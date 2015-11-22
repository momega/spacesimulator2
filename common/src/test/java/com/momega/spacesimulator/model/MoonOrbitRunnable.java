/**
 * 
 */
package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.builder.VoyageToMoonBuilder;
import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.propagator.PropagationResult;
import com.momega.spacesimulator.propagator.PropagatorService;
import com.momega.spacesimulator.service.ManeuverService;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * @author martin
 *
 */
@Component
@Scope("prototype")
public class MoonOrbitRunnable implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(MoonOrbitRunnable.class);
	
	private Timestamp timestamp;
	    
	private double speed;
	
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
	public void run() {
        logger.info("Start at = {}", TimeUtils.toDateTime(timestamp).toString());
        
        VoyageToMoonBuilder mob = applicationContext.getBean(VoyageToMoonBuilder.class);
        mob.setSpeed(speed);
        Assert.assertNotNull(mob);

        Model model = mob.build();
        mob.computeInitInstants(timestamp);
        
        Spacecraft spacecraft = modelService.findAllSpacecrafts(model).get(0);
        Assert.assertNotNull(spacecraft);

        Instant si = instantManager.getInstant(model, spacecraft, timestamp);

        logger.info("Satellite start : {}", si.getCartesianState());

        double rStart = si.getCartesianState().getPosition().getNorm();
        double vStart = si.getCartesianState().getVelocity().getNorm();
        logger.debug("r-start = {}, v-start = {}", rStart, vStart);

        List<MovingObject> list = new ArrayList<>();
        list.add(spacecraft);

        Timestamp tBurn = TimeUtils.fromDateTime(new DateTime(2014, 9, 16, 5, 10, DateTimeZone.UTC));
        TimeInterval timeInterval = new TimeInterval();
        timeInterval.setStartTime(timestamp);
        timeInterval.setEndTime(tBurn);

        Maneuver m = new Maneuver();
        m.setThrottle(1.0);
        m.setInterval(TimeUtils.createInterval(tBurn, 3600));
        m.setThrottleAlpha(Math.PI);
        maneuverService.addManeuver(m, spacecraft);
        
        PropagationResult result = propagatorService.propagateTrajectories(model, list, timeInterval, 1);
        si = result.getInstants().get(spacecraft);
        Assert.assertNotNull(si);
      
        KeplerianElements burnKe = si.getTargetData().getKeplerianElements();
        logger.warn("orbit at start of the burn = {}", burnKe);
        double minimumVel = si.getTargetData().getCartesianState().getVelocity().getNorm();
        logger.warn("velocity at the burn start {}", minimumVel);

        result = propagatorService.propagateTrajectories(model, list, m.getInterval(), 1);
        si = result.getInstants().get(spacecraft);
        Assert.assertNotNull(si);
        
        KeplerianElements endKe = si.getTargetData().getKeplerianElements();
        minimumVel = si.getTargetData().getCartesianState().getVelocity().getNorm();
        logger.warn("orbit at end of the burn = {}", endKe);
        logger.warn("spacecraft state at end of the burn = {}", si.getSpacecraftState());
        logger.warn("velocity at the burn end {}", minimumVel);
        
        Assert.assertTrue(endKe.getKeplerianOrbit().getEccentricity()<1);
	}
	
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}

}
