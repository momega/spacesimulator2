package com.momega.spacesimulator.model;

import com.momega.spacesimulator.builder.SunEarthMoonBuilder;
import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.propagator.PropagationResult;
import com.momega.spacesimulator.propagator.PropagatorService;
import com.momega.spacesimulator.propagator.force.EarthGravityFilter;
import com.momega.spacesimulator.propagator.force.GravityModel;
import com.momega.spacesimulator.service.ApsisService;
import com.momega.spacesimulator.service.CoordinateService;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.utils.TimeUtils;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 8/25/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SimpleConfig.class})
public class SunEarthMoonTest {

    private static final Logger logger = LoggerFactory.getLogger(EarthMoonTest.class);

    @Autowired
    private SunEarthMoonBuilder mob;

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

    @Test
    public void sunEarthMoonTest() {
        gravityModel.setGravityFilter(new EarthGravityFilter());
        Assert.assertNotNull(mob);

        Model model = mob.build();
        CelestialBody earth = (CelestialBody) modelService.findByName(model, "Earth");

        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setName("Satellite");
        model.getMovingObjects().add(spacecraft);

        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2015, 9, 23, 12, 0, DateTimeZone.UTC));
        mob.init(timestamp);

        KeplerianOrbit craftOrbit = mob.createKeplerianOrbit(earth.getReferenceFrameDefinition(), 250 * 1E3 + earth.getRadius(), 0.001, 0, 90.0 * 60, timestamp, 0, 0);
        Instant si = keplerianPropagator.computeFromOrbit(model, spacecraft, craftOrbit, timestamp);

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
