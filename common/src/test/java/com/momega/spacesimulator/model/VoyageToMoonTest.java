package com.momega.spacesimulator.model;

import com.momega.spacesimulator.builder.EarthMoonBuilder;
import com.momega.spacesimulator.service.CoordinateService;
import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.propagator.PropagationResult;
import com.momega.spacesimulator.propagator.PropagatorService;
import com.momega.spacesimulator.propagator.force.GravityModel;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.service.ApsisService;
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
 * Created by martin on 7/19/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SimpleConfig.class})
public class VoyageToMoonTest {

    private static final Logger logger = LoggerFactory.getLogger(VoyageToMoonTest.class);

    @Autowired
    private EarthMoonBuilder mob;

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
    public void voyagerTest() {
        Assert.assertNotNull(mob);

        Model model = mob.build();
        CelestialBody earth = (CelestialBody) modelService.findByName(model, "Earth");
        CelestialBody moon = (CelestialBody) modelService.findByName(model, "Moon");

        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setName("Satellite");
        spacecraft.setTarget(moon);
        spacecraft.setThreshold(50*1E6);
        spacecraft.setMinimalDistance(spacecraft.getThreshold());
        model.getMovingObjects().add(spacecraft);

        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 1, 20, DateTimeZone.UTC));
        mob.init(timestamp);

        CartesianState cartesianState = mob.constructCartesianState(earth, spacecraft, timestamp, 300 * 1E3 + earth.getRadius(), 0, 6.0, 125, 138, 10833d);

        KeplerianElements keplerianElements = coordinateService.transform(cartesianState, timestamp);
        Instant si = instantManager.newInstant(model, spacecraft, cartesianState, keplerianElements, timestamp);

        logger.info("Satellite start : {}", si.getCartesianState());

        double rStart = si.getCartesianState().getPosition().getNorm();
        double vStart = si.getCartesianState().getVelocity().getNorm();
        logger.info("r-start = {}, v-start = {}", rStart, vStart);

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
            logger.info("Instant = {}:{}", i.getMovingObject().getName(), i.getKeplerianElements());
        }

        logger.info("Spacecraft state = {}", si.getCartesianState());

        Instant minimumInstant = spacecraft.getMinimalInstant();
        if (minimumInstant != null) {
            logger.info("Spacecraft minimum = {}", minimumInstant.getTargetData().getCartesianState().getPosition().getNorm() / 1E6);
            logger.info("Spacecraft minimum orbit = {}", si.getKeplerianElements().getKeplerianOrbit());
        }

        double minimum = minimumInstant.getTargetData().getCartesianState().getPosition().getNorm() / 1E6;
        //Assert.assertEquals(6, minimum, 1);

//        logger.info("Spacecraft target data planes angle= {}", Math.toDegrees(si.getTargetData().getPlanesAngle()));
//        logger.info("Spacecraft target data center angle= {}", Math.toDegrees(si.getTargetData().getCentreAngle()));
//        logger.info("Spacecraft target data distance= {}", si.getTargetData().getCartesianState().getPosition().getNorm() / 1E6);
//        logger.info("Spacecraft keplerian orbit= {}", si.getKeplerianElements().getKeplerianOrbit());

//        KeplerianElements ke = coordinateModels.transform(si.getTargetData().getCartesianState(), endTime);
//        logger.info("Target keplerian elements = {}", ke);
//
//        double rEnd = si.getCartesianState().getPosition().getNorm();
//        logger.info("r-end = {}", rEnd / 1E6);
//
//        Instant ai = apsisUtils.getApsis(model, ApsisType.APOAPSIS, si.getKeplerianElements().getKeplerianOrbit(), endTime);
//        double a = ai.getCartesianState().getPosition().getNorm();
//        logger.info("Apo apsis = {}, {}", a, ai.getCartesianState());
    }

}
