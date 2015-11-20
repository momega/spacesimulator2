package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.momega.spacesimulator.builder.EarthMoonBuilder;
import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.propagator.PropagationResult;
import com.momega.spacesimulator.propagator.PropagatorService;
import com.momega.spacesimulator.propagator.force.GravityModel;
import com.momega.spacesimulator.service.ApsisService;
import com.momega.spacesimulator.service.CoordinateService;
import com.momega.spacesimulator.service.ManeuverService;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.utils.KeplerianUtils;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * Created by martin on 11/1/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SimpleConfig.class})
public class PropulsionTest {
	
	private static final Logger logger = LoggerFactory.getLogger(PropulsionTest.class);

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
    
    @Autowired
    private KeplerianUtils keplerianUtils;
    
    @Autowired
    private ManeuverService maneuverService;

    @Test
    public void propulsionTest() {
    	Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2016, 1, 1, 0, 0, DateTimeZone.UTC));
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
        spacecraft.setPropulsion(propulsion);

        mob.init(timestamp);
        
        Maneuver m = new Maneuver();
        m.setThrottle(1.0);
        m.setInterval(TimeUtils.createInterval(timestamp.add(60*10), 120));
        maneuverService.addManeuver(m, spacecraft);
        
        Maneuver m2 = new Maneuver();
        m2.setThrottle(1.0);
        m2.setThrottleAlpha(Math.PI);
        m2.setInterval(TimeUtils.createInterval(timestamp.add(60*60*14), 120));
        maneuverService.addManeuver(m2, spacecraft);
        
        CartesianState cartesianState = mob.constructCartesianState(earth, spacecraft, timestamp, 300 * 1E3 + earth.getRadius(), 0, 6.0, 125, 138, speed);

        KeplerianElements keplerianElements = coordinateService.transform(cartesianState, timestamp);
        logger.warn("init keplerian elements = {}", keplerianElements);
        Instant si = instantManager.newInstant(model, spacecraft, cartesianState, keplerianElements, timestamp);
        mob.initSpacecraftState(spacecraft, propulsion, si);
        logger.warn("init velocity = {}", si.getCartesianState().getVelocity().getNorm());
        double ivp = keplerianUtils.periapsisVelocity(keplerianElements.getKeplerianOrbit());
        logger.warn("init velocity 2 = {}", ivp);

        Timestamp endTime = timestamp.add(60*60*12);
        TimeInterval timeInterval = TimeUtils.createInterval(timestamp, endTime);
        
        List<MovingObject> list = new ArrayList<>();
        list.add(spacecraft);
        PropagationResult result = propagatorService.propagateTrajectories(model, list, timeInterval, 1);
        Instant ei = result.getInstants().get(spacecraft);
        
        logger.warn("end keplerian elements = {}", ei.getKeplerianElements());
        logger.warn("end velocity = {}", ei.getCartesianState().getVelocity().getNorm());
        double evp = keplerianUtils.periapsisVelocity(ei.getKeplerianElements().getKeplerianOrbit());
        logger.warn("end velocity 2 = {}", evp);
        
        Assert.assertTrue("end velocity has to be greater than initial", evp>ivp);
        
        Timestamp endTime2 = timestamp.add(60*60*17);
        TimeInterval timeInterval2 = TimeUtils.createInterval(endTime, endTime2);
        
        PropagationResult result2 = propagatorService.propagateTrajectories(model, list, timeInterval2, 1);
        Instant ei2 = result2.getInstants().get(spacecraft);
        
        logger.warn("end keplerian elements = {}", ei2.getKeplerianElements());
        logger.warn("end velocity = {}", ei2.getCartesianState().getVelocity().getNorm());
        double evp2 = keplerianUtils.periapsisVelocity(ei2.getKeplerianElements().getKeplerianOrbit());
        logger.warn("end velocity 2 = {}", evp2);
    }
}
