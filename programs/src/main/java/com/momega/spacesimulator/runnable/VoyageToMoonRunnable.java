package com.momega.spacesimulator.runnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.builder.VoyageToMoonBuilder;
import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.TimeInterval;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.propagator.PropagationResult;
import com.momega.spacesimulator.propagator.PropagatorService;
import com.momega.spacesimulator.propagator.force.GravityModel;
import com.momega.spacesimulator.service.ApsisService;
import com.momega.spacesimulator.service.CoordinateService;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * Created by martin on 7/19/15.
 */
@Component
@Scope("prototype")
public class VoyageToMoonRunnable implements Callable<VoyageToMoonResult> {

    private static final Logger logger = LoggerFactory.getLogger(VoyageToMoonRunnable.class);
    
    private Timestamp timestamp;
    
    private double speed;

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

    public VoyageToMoonResult call() {
        logger.info("Start at = {} with speed", TimeUtils.toDateTime(timestamp).toString(), speed);

        VoyageToMoonBuilder mob = applicationContext.getBean(VoyageToMoonBuilder.class);
        mob.setSpeed(speed);
        Assert.notNull(mob);

        Model model = mob.build();
        mob.computeInitInstants(timestamp);
        
        CelestialBody moon = (CelestialBody) modelService.findByName(model, "Moon");
        
        Spacecraft spacecraft = modelService.findAllSpacecrafts(model).get(0);
        Assert.notNull(spacecraft);

        Instant si = instantManager.getInstant(model, spacecraft, timestamp);

        logger.info("Satellite start : {}", si.getCartesianState());

        double rStart = si.getCartesianState().getPosition().getNorm();
        double vStart = si.getCartesianState().getVelocity().getNorm();
        logger.debug("r-start = {}, v-start = {}", rStart, vStart);

        List<MovingObject> list = new ArrayList<>();
        list.add(spacecraft);

        Timestamp endTime = timestamp.add(60*60*24*4);
        TimeInterval timeInterval = new TimeInterval();
        timeInterval.setStartTime(timestamp);
        timeInterval.setEndTime(endTime);

        PropagationResult propagationResult = propagatorService.propagateTrajectories(model, list, timeInterval, 1);
        si = propagationResult.getInstants().get(spacecraft);
        Assert.notNull(si);

        Assert.isTrue(3 == propagationResult.getInstants().size());

        for(Instant i : propagationResult.getInstants().values()) {
            Assert.notNull(i);
            logger.debug("Instant = {}:{}", i.getMovingObject().getName(), i.getKeplerianElements());
        }

        logger.info("Spacecraft state = {}", si.getCartesianState());
        
        Instant minimumInstant = spacecraft.getMinimalInstant();
        if (minimumInstant != null) {
        	VoyageToMoonResult result = new VoyageToMoonResult();
        	result.distance = minimumInstant.getTargetData().getCartesianState().getPosition().getNorm();
        	result.velocity = minimumInstant.getTargetData().getCartesianState().getVelocity().getNorm();
        	result.keplerianElements = minimumInstant.getTargetData().getKeplerianElements();
        	result.speed = speed;
        	result.minTimestamp = minimumInstant.getTimestamp();
        	result.timestamp = timestamp;
        	result.surface = minimumInstant.getTargetData().getCartesianState().getPosition().getNorm() - moon.getRadius();
        	
        	double diff = TimeUtils.getDuration(timeInterval.getStartTime(), minimumInstant.getTimestamp());
        	result.duration = diff;
        	return result;
        }
        
        return null;
    }
    
    public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
    
    public void setSpeed(double speed) {
		this.speed = speed;
	}

}
