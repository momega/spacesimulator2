package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.service.builder.EarthMoonBuilder;
import com.momega.spacesimulator.service.propagator.KeplerianPropagator;
import com.momega.spacesimulator.service.propagator.PropagationResult;
import com.momega.spacesimulator.service.propagator.PropagatorService;
import com.momega.spacesimulator.service.propagator.force.GravityModel;
import com.momega.spacesimulator.service.utils.TimeUtils;

/**
 * Created by martin on 7/19/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class EarthMoonTest {

    private static final Logger logger = LoggerFactory.getLogger(EarthMoonTest.class);

    @Autowired
    @Qualifier("earthMoonBuilder")
    private EarthMoonBuilder mob;

    @Autowired
    private ModelService modelService;

    @Autowired
    private PropagatorService propagatorService;

    @Autowired
    private KeplerianPropagator keplerianPropagator;

    @Autowired
    private GravityModel gravityModel;

    @Test
    public void earthMoonTest() {
        Model model = mob.build();
        CelestialBody earth = (CelestialBody) modelService.findByName(model, "Earth");

        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setName("Satellite");
        model.getMovingObjects().add(spacecraft);

        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2015, 9, 23, 12, 0, DateTimeZone.UTC));
        mob.computeInitInstants(timestamp);

        KeplerianOrbit craftOrbit = mob.createKeplerianOrbit(earth.getReferenceFrameDefinition(), 250 * 1E3 + earth.getRadius(), 0.001, 0, 90.0 * 60, timestamp, 0, 0);
        Instant si = keplerianPropagator.computeFromOrbit(model, spacecraft, craftOrbit, timestamp);

        double rStart = si.getCartesianState().getPosition().getNorm();
        logger.info("r-start = {}", rStart);
        Assert.assertEquals(6621372.0, rStart, 1.0);

        List<MovingObject> list = new ArrayList<>();
        list.add(spacecraft);

        TimeInterval timeInterval = new TimeInterval();
        timeInterval.setStartTime(timestamp);
        timeInterval.setEndTime(timestamp.add(90.0 * 60));

        PropagationResult result = propagatorService.propagateTrajectories(model, list, timeInterval, 0.01);
        si = result.getInstants().get(spacecraft);
        Assert.assertNotNull(si);

        Assert.assertEquals(3, result.getInstants().size());

        for(Instant i : result.getInstants().values()) {
            Assert.assertNotNull(i);
            logger.info("Instant = {}:{}", i.getMovingObject().getName(), i.getKeplerianElements());
        }

        double rEnd = si.getCartesianState().getPosition().getNorm();
        logger.info("r-end = {}", rEnd);

        Assert.assertEquals(6621372.0, rEnd, 500.0);
    }

}
