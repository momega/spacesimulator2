package com.momega.spacesimulator.model;

import com.momega.spacesimulator.builder.MovingObjectBuilder;
import com.momega.spacesimulator.common.CoordinateModels;
import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.dynamic.ReferenceFrameManager;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.propagator.NewtonianPropagator;
import com.momega.spacesimulator.utils.TimeUtils;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
        Assert.assertNotNull(mob);

        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2015, 9, 23, 12, 0, DateTimeZone.UTC));

        Model model = new Model();

        CelestialBody earth = new CelestialBody();
        earth.setName("Earth");
        mob.updateMovingObject(earth, 5.97219);

        ReferenceFrame referenceFrame = rfm.createCentralReferenceFrame(earth, timestamp);

        mob.insertMovingObject(model, earth);

        CelestialBody moon = new CelestialBody();
        moon.setName("Moon");
        mob.updateMovingObject(moon, 0.07349);
        mob.createKeplerianOrbit(moon, referenceFrame, timestamp, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.145, 208.1199);

        mob.insertMovingObject(model, moon);

        Spacecraft spacecraft = new Spacecraft();
        mob.insertMovingObject(model, spacecraft);

        //Instant instant = initSpacecrafts(applicationContext, model, spacecraft, referenceFrame, earth, timestamp);

        KeplerianPropagator keplerianPropagator = applicationContext.getBean(KeplerianPropagator.class);
        NewtonianPropagator newtonianPropagator = applicationContext.getBean(NewtonianPropagator.class);

        for(int i=0; i<90*60; i++) {
            timestamp = timestamp.add(1);
            Instant instant = keplerianPropagator.compute(model, moon, timestamp);
            //instant = newtonianPropagator.compute(model, instant, timestamp, 1d);

            logger.info("moon at {}, ke = {}", timestamp, instant.getKeplerianElements());
        }
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
        ke.setTrueAnomaly(180.0);
        ke.setTimestamp(timestamp);

        CartesianState cartesianState = coordinateModels.transform(ke);

        Instant instant = instantManager.newInstant(model, spacecraft, cartesianState, ke);
        return instant;
    }
}
