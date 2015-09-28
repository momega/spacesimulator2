package com.momega.spacesimulator.model;

import com.momega.spacesimulator.builder.MovingObjectBuilder;
import com.momega.spacesimulator.dynamic.ReferenceFrameFactory;
import com.momega.spacesimulator.propagator.PropagatorService;
import com.momega.spacesimulator.propagator.model.EarthGravityFilter;
import com.momega.spacesimulator.propagator.model.GravityModel;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.propagator.PropagationResult;
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
 * Created by martin on 8/25/15.
 */
public class SunEarthMoonTest {

    private static final Logger logger = LoggerFactory.getLogger(EarthMoonTest.class);

    @Test
    public void sunEarthMoonTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SimpleConfig.class);
        MovingObjectBuilder mob = applicationContext.getBean(MovingObjectBuilder.class);
        ReferenceFrameFactory rff = applicationContext.getBean(ReferenceFrameFactory.class);
        PropagatorService propagatorService = applicationContext.getBean(PropagatorService.class);

        GravityModel gravityModel = applicationContext.getBean(GravityModel.class);
        gravityModel.setGravityFilter(new EarthGravityFilter());

        Assert.assertNotNull(mob);

        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2015, 9, 23, 12, 0, DateTimeZone.UTC));

        Model model = new Model();

        CelestialBody sun = new CelestialBody();
        sun.setName("Sun");
        mob.updateMovingObject(sun, 1.989 * 1E6, 696.342, 25.05, 0d, 286.13, 63.87);
        ReferenceFrameDefinition sunDefinition = rff.createDefinition(sun, null);
        model.setRootReferenceFrameDefinition(sunDefinition);

        mob.insertKeplerianObject(model, sun, timestamp);

        BaryCentre earthMoonBarycenter = new BaryCentre();
        earthMoonBarycenter.setName("Earth-Moon Barycenter");
        mob.createAndSetKeplerianOrbit(earthMoonBarycenter, sunDefinition, 149598.261d * 1E6, 0.0166739, 287.5824, 365.256814, 2456661.138788696378, 0.0018601064, 175.395d);

        CelestialBody earth = new CelestialBody();
        earth.setName("Earth");
        mob.updateMovingObject(earth, 5.97219, 6.378, 0.997269, 0d, 90d, 190.147d);

        CelestialBody moon = new CelestialBody();
        moon.setName("Moon");
        mob.updateMovingObject(moon, 0.07349, 1.737, 27.321, 269.9949, 66.5392, 38.3213);

        mob.addToBaryCentre(earthMoonBarycenter, earth);
        mob.addToBaryCentre(earthMoonBarycenter, moon);

        mob.insertKeplerianObject(model, earthMoonBarycenter, timestamp);

        ReferenceFrameDefinition earthMoonDefinition = rff.createDefinition(earthMoonBarycenter, sunDefinition);
        mob.createAndSetKeplerianOrbit(earth, earthMoonDefinition, 4.686955382086 * 1E6, 0.055557, 264.7609, 27.427302, 2456796.39770, 5.241500, 208.1199);
        mob.createAndSetKeplerianOrbit(moon, earthMoonDefinition, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.241500, 208.1199);

        mob.insertKeplerianObject(model, earth, timestamp);
        mob.insertKeplerianObject(model, moon, timestamp);

        ReferenceFrameDefinition earthDefinition = rff.createDefinition(earth, earthMoonDefinition);

        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setName("Satellite");
        KeplerianOrbit craftOrbit = mob.createKeplerianOrbit(earthDefinition, 250 * 1E3 + earth.getRadius(), 0.001, 0, 90.0 * 60, timestamp, 0, 0);
        Instant si = mob.insertSpacecraft(model, spacecraft, craftOrbit, timestamp);

        double rStart = si.getCartesianState().getPosition().getNorm();
        logger.info("r-start = {}", rStart);
        Assert.assertEquals(6621372.0, rStart, 1.0);

        logger.info("Instant = {}:{}", si.getMovingObject().getName(), si.getKeplerianElements());

        TimeInterval timeInterval = new TimeInterval();
        timeInterval.setStartTime(timestamp);
        timeInterval.setEndTime(timestamp.add(60*90));

        List<MovingObject> list = new ArrayList<>();
        list.add(spacecraft);

        PropagationResult result = propagatorService.propagateTrajectories(model, list, timeInterval, 0.02);

        Assert.assertEquals(5, result.getInstants().size());

        for(Instant i : result.getInstants().values()) {
            Assert.assertNotNull(i);
            logger.info("Instant = {}:{}", i.getMovingObject().getName(), i.getKeplerianElements());
        }

        si = result.getInstants().get(spacecraft);
        double rEnd = si.getCartesianState().getPosition().getNorm();
        logger.info("r-end = {}", rEnd);
        Assert.assertEquals(rStart, rEnd, 2.0);


    }
}
