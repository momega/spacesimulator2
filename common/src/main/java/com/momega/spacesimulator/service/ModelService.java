package com.momega.spacesimulator.service;

import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.propagator.NewtonianPropagator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
        List<CelestialBody> result = model.getMovingObjects().stream().
                filter(movingObject -> movingObject instanceof CelestialBody).
                map(movingObject -> (CelestialBody) movingObject).
                collect(Collectors.toList());
        return result;
    }

    /**
     * Propagates the trajectory of the moving object in the given time interval
     * @param model the model
     * @param movingObjects the set moving objects
     * @param timeInterval the time interval
     * @param dt the step
     */
    public void propagateTrajectories(Model model, List<MovingObject> movingObjects, TimeInterval timeInterval, double dt) {
        Timestamp time = timeInterval.getStartTime();
        Timestamp newTime = time.add(dt);
        while (!newTime.after(timeInterval.getEndTime())) {
            for(MovingObject movingObject : movingObjects) {
                Instant instant = instantManager.getInstant(model, movingObject, newTime);
                if (instant == null) {
                    instant = propagateTrajectory(model, movingObject, time, newTime, dt);
                    logger.info("{} : {}", movingObject.getName(), instant.getKeplerianElements());
                }
            }
            time = newTime;
            newTime = time.add(dt);
        }
    }

    protected Instant propagateTrajectory(Model model, MovingObject movingObject, Timestamp timestamp, Timestamp newTimestamp, double dt) {
        if (movingObject instanceof Spacecraft) {
            return newtonianPropagator.compute(model, movingObject, timestamp, newTimestamp, dt);
        } else {
            CelestialBody celestialBody = (CelestialBody) movingObject;
            return keplerianPropagator.compute(model, celestialBody, newTimestamp);
        }
    }

}
