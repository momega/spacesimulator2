package com.momega.spacesimulator.model;

import com.momega.spacesimulator.builder.MovingObjectBuilder;
import com.momega.spacesimulator.dynamic.ReferenceFrameFactory;
import com.momega.spacesimulator.propagator.PropagatorService;
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
 * Created by martin on 7/19/15.
 */
public class VoyageToMoonTest {

    private static final Logger logger = LoggerFactory.getLogger(VoyageToMoonTest.class);

    @Test
    public void voyagerTest() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SimpleConfig.class);
        MovingObjectBuilder mob = applicationContext.getBean(MovingObjectBuilder.class);
        ReferenceFrameFactory rff = applicationContext.getBean(ReferenceFrameFactory.class);
        PropagatorService propagatorService = applicationContext.getBean(PropagatorService.class);

        Assert.assertNotNull(mob);

        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 23, 12, 0, DateTimeZone.UTC));

        Model model = new Model();

        CelestialBody earth = new CelestialBody();
        earth.setName("Earth");
        ReferenceFrameDefinition rootDefinition = rff.createDefinition(earth, null);
        model.setRootReferenceFrameDefinition(rootDefinition);

        mob.updateMovingObject(earth, 5.97219, 6.378, 0.997269, 190.147d, 0d, 90d);
        mob.insertKeplerianObject(model, earth, timestamp);

        CelestialBody moon = new CelestialBody();
        moon.setName("Moon");
        mob.updateMovingObject(moon, 0.07349, 1.737, 27.321, 38.3213d, 269.9949d, 66.5392d);
        mob.createAndSetKeplerianOrbit(moon, rootDefinition, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.145, 208.1199);

        mob.insertKeplerianObject(model, moon, timestamp);

        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setName("Satellite");
        spacecraft.setTarget(moon);

        KeplerianOrbit craftOrbit = mob.createKeplerianOrbit(rootDefinition, 400 * 1E3 + earth.getRadius(), 0.2, 0, 90.0 * 60, timestamp, 7, 0);
        Instant si = mob.insertSpacecraft(model, spacecraft, craftOrbit, timestamp);

        logger.info("Satellite start : {}", si.getCartesianState());

        double rStart = si.getCartesianState().getPosition().getNorm();
        logger.info("r-start = {}", rStart);

        List<MovingObject> list = new ArrayList<>();
        list.add(spacecraft);

        TimeInterval timeInterval = new TimeInterval();
        timeInterval.setStartTime(timestamp);
        timeInterval.setEndTime(timestamp.add(60*60*24));

        PropagationResult result = propagatorService.propagateTrajectories(model, list, timeInterval, 0.1);
        si = result.getInstants().get(spacecraft);
        Assert.assertNotNull(si);

        Assert.assertEquals(3, result.getInstants().size());

        for(Instant i : result.getInstants().values()) {
            Assert.assertNotNull(i);
            logger.info("Instant = {}:{}", i.getMovingObject().getName(), i.getKeplerianElements());
        }

        logger.info("Spacecraft state = {}", si.getCartesianState());
        logger.info("Spacecraft target data = {}", si.getTargetData());

        double rEnd = si.getCartesianState().getPosition().getNorm();
        logger.info("r-end = {}", rEnd);

//        Assert.assertEquals(6621372.0, rEnd, 10.0);
    }

}
