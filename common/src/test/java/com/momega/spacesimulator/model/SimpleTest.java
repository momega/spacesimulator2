package com.momega.spacesimulator.model;

import com.momega.spacesimulator.builder.MovingObjectBuilder;
import com.momega.spacesimulator.dynamic.ReferenceFrameManager;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
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

        KeplerianPropagator keplerianPropagator = applicationContext.getBean(KeplerianPropagator.class);

        for(int i=0; i<28; i++) {
            timestamp = timestamp.add(DateTimeConstants.SECONDS_PER_DAY);
            Instant instant = keplerianPropagator.compute(model, moon, timestamp);

            logger.info("moon at {}, ke = {}", timestamp, instant.getKeplerianElements());
        }
    }
}
