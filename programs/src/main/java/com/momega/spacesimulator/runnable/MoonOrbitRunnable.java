/**
 * 
 */
package com.momega.spacesimulator.runnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.TimeInterval;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.InstantManager;
import com.momega.spacesimulator.service.ManeuverService;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.service.builder.VoyageToMoonBuilder;
import com.momega.spacesimulator.service.propagator.PropagationResult;
import com.momega.spacesimulator.service.propagator.PropagatorService;
import com.momega.spacesimulator.service.utils.TimeUtils;

/**
 * @author martin
 *
 */
@Component
@Scope("prototype")
public class MoonOrbitRunnable implements Callable<MoonOrbitResult> {
	
	private static final Logger logger = LoggerFactory.getLogger(MoonOrbitRunnable.class);
	
	private Timestamp timestamp;
	    
	private double speed;
	
	private double startBurnTime;
	
	private double burnTime;
	
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

	@Override
	public MoonOrbitResult call() {
        logger.info("Start at = {}", TimeUtils.toDateTime(timestamp).toString());
        
        VoyageToMoonBuilder mob = applicationContext.getBean(VoyageToMoonBuilder.class);
        mob.setSpeed(speed);
        Assert.notNull(mob);

        Model model = mob.build();
        mob.computeInitInstants(timestamp);
        
        CelestialBody moon = (CelestialBody) modelService.findByName(model, "Moon");
        
        Spacecraft spacecraft = modelService.findAllSpacecrafts(model).get(0);
        Assert.notNull(spacecraft);

        Instant si = instantManager.getInstant(model, spacecraft, timestamp);

        logger.info("Satellite start : {}", si.getCartesianState());

        double rStart = si.getCartesianState().getPosition().getNorm();
        double vStart = si.getCartesianState().getVelocity().getNorm();
        logger.debug("r-start = {}, v-start = {}", rStart, vStart);

        List<MovingObject> list = new ArrayList<>();
        list.add(spacecraft);

        Timestamp tBurn = timestamp.add(startBurnTime);
        TimeInterval timeInterval = new TimeInterval();
        timeInterval.setStartTime(timestamp);
        timeInterval.setEndTime(tBurn);

        Maneuver m = new Maneuver();
        m.setThrottle(1.0);
        m.setInterval(TimeUtils.createInterval(tBurn, burnTime));
        m.setThrottleAlpha(Math.PI);
        maneuverService.addManeuver(m, spacecraft);
        
        PropagationResult result = propagatorService.propagateTrajectories(model, list, timeInterval, 1);
        si = result.getInstants().get(spacecraft);
        Assert.notNull(si);
      
        KeplerianElements burnKe = si.getTargetData().getKeplerianElements();
        logger.info("orbit at start of the burn = {}", burnKe);
        double velocity = si.getTargetData().getCartesianState().getVelocity().getNorm();
        logger.info("velocity at the burn start {}", velocity);
        logger.info("distance at the burn start {}", si.getTargetData().getCartesianState().getPosition().getNorm());

        result = propagatorService.propagateTrajectories(model, list, m.getInterval(), 1);
        si = result.getInstants().get(spacecraft);
        Assert.notNull(si);
        
        KeplerianElements endKe = si.getTargetData().getKeplerianElements();
        velocity = si.getTargetData().getCartesianState().getVelocity().getNorm();
        logger.info("spacecraft state at end of the burn = {}", si.getSpacecraftState());
        logger.info("velocity at the burn end {}");
        
        //Assert.assertTrue(endKe.getKeplerianOrbit().getEccentricity()<1);
        MoonOrbitResult r = new MoonOrbitResult();
        r.burnTime = burnTime;
        r.startBurnTime = startBurnTime;
        r.eccentricity = endKe.getKeplerianOrbit().getEccentricity();
        r.period = endKe.getKeplerianOrbit().getPeriod();
        r.perilune = endKe.getKeplerianOrbit().getSemimajorAxis() * (1 - endKe.getKeplerianOrbit().getEccentricity()) - moon.getRadius();
        r.apolune = endKe.getKeplerianOrbit().getSemimajorAxis() * (1 + endKe.getKeplerianOrbit().getEccentricity()) - moon.getRadius();
        r.keplerianElements = endKe;
        r.velocity = velocity;
        
        logger.info("orbit at end of the burn = {}, end vel = {}, perimoon {}km, start burn = {}, burn time = {}", r.keplerianElements, r.velocity, r.perilune / 1000, r.apolune/1000, startBurnTime, burnTime);
        
        return r;
	}
	
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setStartBurnTime(double burnTime) {
		this.startBurnTime = burnTime;
	}
	
	public void setBurnTime(double burnTime) {
		this.burnTime = burnTime;
	}

}
