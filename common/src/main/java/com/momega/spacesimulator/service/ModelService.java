package com.momega.spacesimulator.service;

import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.propagator.NewtonianPropagator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by martin on 7/25/15.
 */
@Component
public class ModelService {

    private static final Logger logger = LoggerFactory.getLogger(ModelService.class);

    @Autowired
    private InstantManager instantManager;

    @Autowired
    private NewtonianPropagator newtonianPropagator;

    @Autowired
    private KeplerianPropagator keplerianPropagator;

    public List<CelestialBody> findAllCelestialBodies(Model model) {
        List<CelestialBody> result = new ArrayList();
        for(MovingObject mo : model.getMovingObjects()) {
            if (mo instanceof CelestialBody) {
                result.add((CelestialBody) mo);
            }
        }
        return result;
    }

    public List<KeplerianObject> findAllKeplerianObjects(Model model) {
        List<KeplerianObject> result = new ArrayList();
        for(MovingObject mo : model.getMovingObjects()) {
            if (mo instanceof KeplerianObject) {
                result.add((KeplerianObject) mo);
            }
        }
        return result;
    }

    /**
     * Propagates the trajectory of the moving object in the given time interval.It is assumed that starting point of the interval
     * is already calculated. The iteration is run until end time of the interval (inclusive)
     * @param model the model
     * @param movingObjects the set moving objects
     * @param timeInterval the time interval.
     * @param dt the step
     */
    public PropagationResult propagateTrajectories(Model model, List<MovingObject> movingObjects, TimeInterval timeInterval, double dt) {
        PropagationResult result = new PropagationResult();
        Timestamp time = timeInterval.getStartTime();
        result.setStartTime(time);
        long t1 = System.nanoTime();
        logger.info("propagation started at {}", time);
        while (!time.after(timeInterval.getEndTime())) {
            Timestamp newTime = time.add(dt);
            for(MovingObject movingObject : movingObjects) {
                Instant instant = instantManager.getInstant(model, movingObject, newTime);
                if (instant == null) {
                    instant = propagateTrajectory(model, movingObject, time, newTime, dt);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("{} : {}, {}", movingObject.getName(), instant.getCartesianState(), instant.getKeplerianElements());
                }
            }
            time = newTime;
        }
        result.setEndTime(time);
        long t2 = System.nanoTime();
        result.setExecTime(t2 - t1);
        logger.info("propagation finished at {} in {}ns ", time, result.getExecTime());
        result.setInstants(instantManager.getInstants(model, time));
        return result;
    }

    protected Instant propagateTrajectory(Model model, MovingObject movingObject, Timestamp timestamp, Timestamp newTimestamp, double dt) {
        if (movingObject instanceof Spacecraft) {
            return newtonianPropagator.compute(model, movingObject, timestamp, newTimestamp, dt);
        } else {
            KeplerianObject keplerianObject = (KeplerianObject) movingObject;
            return keplerianPropagator.compute(model, keplerianObject, newTimestamp);
        }
    }

    public void setInstantManager(InstantManager instantManager) {
        this.instantManager = instantManager;
    }

    public void setKeplerianPropagator(KeplerianPropagator keplerianPropagator) {
        this.keplerianPropagator = keplerianPropagator;
    }
}
