package com.momega.spacesimulator.service.propagator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.KeplerianObject;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.TimeInterval;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.InstantManager;
import com.momega.spacesimulator.service.propagator.feature.PropagatorFeature;
import com.momega.spacesimulator.service.utils.TimeUtils;

/**
 * Created by martin on 9/28/15.
 */
@Component
public class PropagatorService {

    private static final Logger logger = LoggerFactory.getLogger(PropagatorService.class);

    @Autowired
    private InstantManager instantManager;

    @Autowired
    private NewtonianPropagator newtonianPropagator;

    @Autowired
    private KeplerianPropagator keplerianPropagator;

    @Autowired
    private List<PropagatorFeature> features = new ArrayList<>();

    /**
     * Propagates the trajectory of the moving object in the given time interval.It is assumed that starting point of the interval
     * is already calculated. The iteration is run until end time of the interval (inclusive)
     * @param model the force
     * @param movingObjects the set moving objects
     * @param timeInterval the time interval.
     * @param dt the step
     */
    public PropagationResult propagateTrajectories(Model model, List<MovingObject> movingObjects, TimeInterval timeInterval, double dt) {
        PropagationResult result = new PropagationResult();
        Timestamp time = timeInterval.getStartTime();
        logger.debug("propagation started at {}", TimeUtils.timeAsString(time));
        result.setStartTime(time);
        long t1 = System.nanoTime();
        while (!time.after(timeInterval.getEndTime())) {
            Timestamp newTime = time.add(dt);
            logger.debug("propagation at {}", newTime);
            for(MovingObject movingObject : movingObjects) {
                Instant instant = instantManager.getInstant(model, movingObject, newTime);
                if (instant == null) {
                    instant = propagateTrajectory(model, movingObject, time, newTime, dt);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("{} : {}, {}", movingObject.getName(), instant.getCartesianState(), instant.getKeplerianElements());
                }
            }

            for(PropagatorFeature feature : features) {
                feature.calculation(model, newTime);
            }

            time = newTime;
        }
        result.setEndTime(time);
        for(MovingObject mo : model.getMovingObjects()) {
            if (mo instanceof KeplerianObject) {
                KeplerianObject keplerianObject = (KeplerianObject) mo;
                keplerianPropagator.compute(model, keplerianObject, time);
            }
        }
        long t2 = System.nanoTime();
        result.setExecTime(t2 - t1);
        logger.debug("propagation finished at {} in {}s ", TimeUtils.timeAsString(time), result.getExecTime()/1000000000.0);
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

    public void setKeplerianPropagator(KeplerianPropagator keplerianPropagator) {
        this.keplerianPropagator = keplerianPropagator;
    }

    public void setInstantManager(InstantManager instantManager) {
        this.instantManager = instantManager;
    }
}
