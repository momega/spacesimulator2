package com.momega.spacesimulator.propagator.force;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;

/**
 * Created by martin on 11/7/15.
 */
public interface ForceModel {

    /**
     * Computes the acceleration based on the given force
     * @param model the model
     * @param spacecraft the spacecraft
     * @param currentState the current state of the spacecraft at the timestamp
     * @param timestamp
     * @param dt
     * @return
     */
    AccelerationResult getAcceleration(Model model, Spacecraft spacecraft, CartesianState currentState, Timestamp timestamp, double dt);

}
