package com.momega.spacesimulator.model;

import com.momega.spacesimulator.builder.MovingObjectBuilder;
import com.momega.spacesimulator.common.CoordinateModels;
import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.dynamic.ReferenceFrameManager;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.service.PropagationResult;
import com.momega.spacesimulator.utils.TimeUtils;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 7/19/15.
 */
public class SimpleTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleTest.class);

    @Test
    public void simpleTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SimpleConfig.class);
        MovingObjectBuilder mob = applicationContext.getBean(MovingObjectBuilder.class);
        ReferenceFrameManager rfm = applicationContext.getBean(ReferenceFrameManager.class);

        ModelService modelService = applicationContext.getBean(ModelService.class);

        Assert.assertNotNull(mob);

        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2015, 9, 23, 12, 0, DateTimeZone.UTC));

        Model model = new Model();

        CelestialBody earth = new CelestialBody();
        earth.setName("Earth");
        mob.updateMovingObject(earth, 5.97219, 6.378);
        mob.insertCentralBody(model, earth);

        ReferenceFrame referenceFrame = rfm.createByCelestialBody(earth);
        model.setRootReferenceFrame(referenceFrame);

        CelestialBody moon = new CelestialBody();
        moon.setName("Moon");
        mob.updateMovingObject(moon, 0.07349, 1.737);
        mob.createKeplerianOrbit(moon, referenceFrame, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.145, 208.1199);

        mob.insertCelestialBody(model, moon, timestamp);

        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setName("Satellite");
        mob.insertSpacecraft(model, spacecraft);

        initSpacecrafts(applicationContext, model, spacecraft, referenceFrame, earth, timestamp);

        List<MovingObject> list = new ArrayList<>();
        //list.add(moon);
        list.add(spacecraft);

        TimeInterval timeInterval = new TimeInterval();
        timeInterval.setStartTime(timestamp);
        timeInterval.setEndTime(timestamp.add(90.0 * 60 - 32));

        PropagationResult result = modelService.propagateTrajectories(model, list, timeInterval, 0.05);
        Instant i = result.getInstants().get(spacecraft);
        Assert.assertNotNull(i);

        logger.info("{}, {}", i.getCartesianState(), i.getKeplerianElements());
    }

    public Instant initSpacecrafts(ApplicationContext applicationContext, Model model, Spacecraft spacecraft, ReferenceFrame referenceFrame, CelestialBody earth, Timestamp timestamp) {
        CoordinateModels coordinateModels = applicationContext.getBean(CoordinateModels.class);
        InstantManager instantManager = applicationContext.getBean(InstantManager.class);

        KeplerianOrbit keplerianOrbit = new KeplerianOrbit();
        keplerianOrbit.setArgumentOfPeriapsis(0);
        keplerianOrbit.setAscendingNode(0);
        keplerianOrbit.setReferenceFrame(referenceFrame);
        keplerianOrbit.setEccentricity(0.001);
        keplerianOrbit.setInclination(0);
        keplerianOrbit.setPeriod(90.0 * 60);
        keplerianOrbit.setSemimajorAxis(250 * 1E3 + earth.getRadius());
        keplerianOrbit.setTimeOfPeriapsis(timestamp);

        KeplerianElements ke = new KeplerianElements();
        ke.setKeplerianOrbit(keplerianOrbit);
        ke.setTrueAnomaly(0.0);

        CartesianState cartesianState = coordinateModels.transform(ke);

        Instant instant = instantManager.newInstant(model, spacecraft, cartesianState, ke, timestamp);
        return instant;
    }
}
