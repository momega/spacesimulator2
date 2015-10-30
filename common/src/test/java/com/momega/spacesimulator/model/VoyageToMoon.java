package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.builder.EarthMoonBuilder;
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
public class VoyageToMoon {

    private static final Logger logger = LoggerFactory.getLogger(VoyageToMoon.class);

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

    public void run(Timestamp timestamp, double speed) {
        logger.info("Start at = {}", TimeUtils.toDateTime(timestamp).toString());

        EarthMoonBuilder mob = applicationContext.getBean(EarthMoonBuilder.class);
        Assert.assertNotNull(mob);

        Model model = mob.build();
        CelestialBody earth = (CelestialBody) modelService.findByName(model, "Earth");
        CelestialBody moon = (CelestialBody) modelService.findByName(model, "Moon");

        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setName("Satellite");
        spacecraft.setTarget(moon);
        spacecraft.setThreshold(1E6);
        spacecraft.setMinimalDistance(spacecraft.getThreshold());
        model.getMovingObjects().add(spacecraft);

        mob.init(timestamp);

        CartesianState cartesianState = mob.constructCartesianState(earth, spacecraft, timestamp, 300 * 1E3 + earth.getRadius(), 0, 6.0, 125, 138, speed);

        KeplerianElements keplerianElements = coordinateService.transform(cartesianState, timestamp);
        Instant si = instantManager.newInstant(model, spacecraft, cartesianState, keplerianElements, timestamp);

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
            double minumumVel = minimumInstant.getTargetData().getCartesianState().getVelocity().getNorm();
            KeplerianOrbit minOrbit = minimumInstant.getTargetData().getKeplerianElements().getKeplerianOrbit();
            logger.debug("Spacecraft minimum = {}", minimum);
            logger.debug("Spacecraft minimum orbit = {}", minOrbit);

            Timestamp tMin = minimumInstant.getTimestamp();
            double diff = TimeUtils.getDuration(timeInterval.getStartTime(), tMin);
            logger.debug("Duration to min {}", TimeUtils.durationAsString(diff));

            logger.warn("Start at = {}, speed = {}, Dist = {}, velocity = {}, e = {}, at {}", TimeUtils.toDateTime(timestamp).toString(), speed, minimum, minumumVel, minOrbit.getEccentricity(), TimeUtils.durationAsString(diff));
        }

    }

}
