package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.builder.VoyageToMoonBuilder;
import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.propagator.PropagationResult;
import com.momega.spacesimulator.propagator.PropagatorService;
import com.momega.spacesimulator.propagator.force.GravityModel;
import com.momega.spacesimulator.service.ApsisService;
import com.momega.spacesimulator.service.CoordinateService;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * Created by martin on 7/19/15.
 */
@Component
@Scope("prototype")
public class VoyageToMoonRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(VoyageToMoonRunnable.class);
    
    private Timestamp timestamp;
    
    private double speed;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ModelService modelService;

    @Autowired
    private PropagatorService propagatorService;

    @Autowired
    private KeplerianPropagator keplerianPropagator;

    @Autowired
    private GravityModel gravityModel;

    @Autowired
    private CoordinateService coordinateService;

    @Autowired
    private ApsisService apsisService;

    @Autowired
    private InstantManager instantManager;

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

        Timestamp endTime = timestamp.add(60*60*24*5);
        TimeInterval timeInterval = new TimeInterval();
        timeInterval.setStartTime(timestamp);
        timeInterval.setEndTime(endTime);

        PropagationResult result = propagatorService.propagateTrajectories(model, list, timeInterval, 1);
        si = result.getInstants().get(spacecraft);
        Assert.assertNotNull(si);

        Assert.assertEquals(3, result.getInstants().size());

        for(Instant i : result.getInstants().values()) {
            Assert.assertNotNull(i);
            logger.debug("Instant = {}:{}", i.getMovingObject().getName(), i.getKeplerianElements());
        }

        logger.info("Spacecraft state = {}", si.getCartesianState());

        Instant minimumInstant = spacecraft.getMinimalInstant();
        if (minimumInstant != null) {
            double minimum = minimumInstant.getTargetData().getCartesianState().getPosition().getNorm() / 1E6;
            double minimumVel = minimumInstant.getTargetData().getCartesianState().getVelocity().getNorm();
            KeplerianOrbit minOrbit = minimumInstant.getTargetData().getKeplerianElements().getKeplerianOrbit();
            logger.debug("Spacecraft minimum = {}", minimum);
            logger.debug("Spacecraft minimum orbit = {}", minOrbit);

            Timestamp tMin = minimumInstant.getTimestamp();
            logger.warn("timestamp at minimum = {}", TimeUtils.timeAsString(tMin));
            double diff = TimeUtils.getDuration(timeInterval.getStartTime(), tMin);
            logger.debug("Duration to min {}", TimeUtils.durationAsString(diff));

            logger.warn("Start at = {}, speed = {}, Dist = {}, velocity = {}, e = {}, at {}", TimeUtils.timeAsString(timestamp), speed, minimum, minimumVel, minOrbit.getEccentricity(), TimeUtils.durationAsString(diff));
        }

    }
    
    public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
    
    public void setSpeed(double speed) {
		this.speed = speed;
	}

}
