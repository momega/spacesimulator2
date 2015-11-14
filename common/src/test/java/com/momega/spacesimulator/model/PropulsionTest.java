package com.momega.spacesimulator.model;

import com.momega.spacesimulator.builder.EarthMoonBuilder;
import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.propagator.PropagatorService;
import com.momega.spacesimulator.propagator.force.GravityModel;
import com.momega.spacesimulator.service.ApsisService;
import com.momega.spacesimulator.service.CoordinateService;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.utils.TimeUtils;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by martin on 11/1/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SimpleConfig.class})
public class PropulsionTest {

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

    @Test
    @Ignore
    public void propulsionTest() {
        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2015, 11, 12, 15, 0, DateTimeZone.UTC));
        double speed = 8200;

        EarthMoonBuilder mob = applicationContext.getBean(EarthMoonBuilder.class);
        Assert.assertNotNull(mob);

        Model model = mob.build();
        CelestialBody earth = (CelestialBody) modelService.findByName(model, "Earth");

        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setName("Satellite");
        spacecraft.setInitialMass(30000);
        model.getMovingObjects().add(spacecraft);

        Propulsion propulsion = new Propulsion();
        propulsion.setMassFlow(5);
        propulsion.setSpecificImpulse(311);
        propulsion.setTotalFuel(28000);

        mob.init(timestamp);

        CartesianState cartesianState = mob.constructCartesianState(earth, spacecraft, timestamp, 300 * 1E3 + earth.getRadius(), 0, 6.0, 125, 138, speed);

        KeplerianElements keplerianElements = coordinateService.transform(cartesianState, timestamp);
        Instant si = instantManager.newInstant(model, spacecraft, cartesianState, keplerianElements, timestamp);

    }
}
