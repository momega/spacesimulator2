/**
 * 
 */
package com.momega.spacesimulator.simulation.moonorbit;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.model.ApsisType;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.TimeInterval;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.ApsisService;
import com.momega.spacesimulator.service.InstantManager;
import com.momega.spacesimulator.service.ManeuverService;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.service.builder.VoyageToMoonBuilder;
import com.momega.spacesimulator.service.propagator.PropagationResult;
import com.momega.spacesimulator.service.propagator.PropagatorService;
import com.momega.spacesimulator.service.utils.TimeUtils;
import com.momega.spacesimulator.simulation.SimulationSolver;

/**
 * @author martin
 *
 */
@Component
@Scope("prototype")
public class MoonOrbitSolver extends SimulationSolver<MoonOrbitResult> {
	
	private static final Logger logger = LoggerFactory.getLogger(MoonOrbitSolver.class);
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private ModelService modelService;

	@Autowired
	private InstantManager instantManager;
	
	@Autowired
	private ManeuverService maneuverService;

	@Autowired
	private PropagatorService propagatorService;
	
	@Autowired
	private ApsisService apsisService;

	@Override
	public MoonOrbitResult apply(MoonOrbitResult input) {
		Timestamp timestamp = input.timestamp;
		double speed = input.speed;
		double burnTime = input.burnTime;
		Timestamp startBurnTime = input.startBurnTime;
		
        logger.info("Start at = {}, start burn time = {}", TimeUtils.timeAsString(timestamp), TimeUtils.timeAsString(startBurnTime));
        
        VoyageToMoonBuilder mob = applicationContext.getBean(VoyageToMoonBuilder.class);
        mob.setSpeed(speed);
        Assert.notNull(mob);

        Model model = mob.build();
        mob.computeInitInstants(timestamp);
        
        CelestialBody moon = (CelestialBody) modelService.findByName(model, "Moon");
        
        Spacecraft spacecraft = modelService.findAllSpacecrafts(model).get(0);
        Assert.notNull(spacecraft);

        Instant si = instantManager.getInstant(model, spacecraft, timestamp);

        logger.debug("Satellite start : {}", si.getCartesianState());

        double rStart = si.getCartesianState().getPosition().getNorm();
        double vStart = si.getCartesianState().getVelocity().getNorm();
        logger.info("r-start = {}km, v-start = {}", rStart/1000, vStart);

        List<MovingObject> list = new ArrayList<>();
        list.add(spacecraft);

        Timestamp tBurn = startBurnTime;
        TimeInterval timeInterval = new TimeInterval();
        timeInterval.setStartTime(timestamp);
        timeInterval.setEndTime(tBurn);

        Maneuver m = new Maneuver();
        m.setThrottle(1.0);
        m.setInterval(TimeUtils.createInterval(tBurn, burnTime));
        m.setInverse(true);
        m.setReferenceFrameDefinition(moon.getReferenceFrameDefinition());
        maneuverService.addManeuver(m, spacecraft);
        
        PropagationResult result = propagatorService.propagateTrajectories(model, list, timeInterval, 1);
        si = result.getInstants().get(spacecraft);
        Assert.notNull(si);
      
        KeplerianElements burnKe = si.getTargetData().getKeplerianElements();
        logger.info("orbit at start of the burn = {}", burnKe);
        double velocity = si.getTargetData().getCartesianState().getVelocity().getNorm();
        Instant apsis = apsisService.getApsis(model, ApsisType.APOAPSIS, burnKe, tBurn);
        logger.info("projected min distance {}km", (apsis.getCartesianState().getPosition().getNorm() - moon.getRadius())/1000);
        logger.info("velocity at the burn start {}", velocity);
        logger.info("distance at the burn start {}km", si.getTargetData().getCartesianState().getPosition().getNorm()/1000);

        result = propagatorService.propagateTrajectories(model, list, m.getInterval(), 1);
        si = result.getInstants().get(spacecraft);
        Assert.notNull(si);
        logger.info("end time = {}", TimeUtils.timeAsString(result.getEndTime()));
        
        KeplerianElements endKe = si.getTargetData().getKeplerianElements();
        double velocity2 = si.getTargetData().getCartesianState().getVelocity().getNorm();
        logger.info("spacecraft state at end of the burn = {}", si.getSpacecraftState());
        logger.info("velocity at the burn end {}", velocity2);
        logger.info("delta v {}", velocity-velocity2);
        
        MoonOrbitResult r = input;
        r.eccentricity = endKe.getKeplerianOrbit().getEccentricity();
        r.period = endKe.getKeplerianOrbit().getPeriod();
        r.perilune = endKe.getKeplerianOrbit().getSemimajorAxis() * (1 - endKe.getKeplerianOrbit().getEccentricity()) - moon.getRadius();
        r.apolune = endKe.getKeplerianOrbit().getSemimajorAxis() * (1 + endKe.getKeplerianOrbit().getEccentricity()) - moon.getRadius();
        r.velocity = velocity;
        logger.info("final ellipses {}km by {}km", r.perilune / 1000, r.apolune/1000);
        logger.info("start burn = {}, burn time = {}, orbit at end of the burn = {}, end vel = {}, perilune {}km, apolune= {}km", TimeUtils.timeAsString(r.startBurnTime), r.burnTime, endKe, r.velocity, r.perilune / 1000, r.apolune/1000);
        
        return r;
	}

}
