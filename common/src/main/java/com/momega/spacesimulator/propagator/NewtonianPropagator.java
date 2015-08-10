package com.momega.spacesimulator.propagator;

import com.momega.spacesimulator.common.CoordinateModels;
import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.propagator.model.GravityModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * The propagator computes new position of the spacecraft. It uses newtonian gravitation model
 * Created by martin on 7/21/15.
 */
@Component
public class NewtonianPropagator {

    @Autowired
    private GravityModel gravityModel;

    @Autowired
    private CoordinateModels coordinateModels;

    @Autowired
    private InstantManager instantManager;


    public Instant compute(Model model, MovingObject movingObject, Timestamp timestamp, Timestamp newTimestamp, double dt) {
        Instant instant = instantManager.getInstant(model, movingObject, timestamp);
        Assert.notNull(instant);

        CartesianState cartesianState = eulerSolver(model, instant, dt);
        KeplerianElements keplerianElements = coordinateModels.transform(cartesianState, newTimestamp);

        Instant newInstant = instantManager.newInstant(model, movingObject, cartesianState, keplerianElements, newTimestamp);
        return newInstant;
    }

    /**
     * Solves the velocity and position by the simple Euler method
     * @param spacecraft the spacecraft instance
     * @param dt time interval
     * @return new cartesian state
     */
    protected CartesianState eulerSolver(Model model, Instant spacecraft, double dt) {
        // Euler's method
        Vector3D position = spacecraft.getCartesianState().getPosition();
        Vector3D velocity = spacecraft.getCartesianState().getVelocity();
        ReferenceFrame referenceFrame = spacecraft.getCartesianState().getReferenceFrame();

        // iterate all force models
        Vector3D acceleration = gravityModel.getAcceleration(model, spacecraft);

        velocity = velocity.add(dt, acceleration); // velocity: v(i) = v(i) + a(i) * dt
        position = position.add(dt, velocity); // position: r(i) = r(i) * v(i) * dt
        // cartesian state
        CartesianState result = new CartesianState();
        result.setVelocity(velocity);
        result.setPosition(position);
        result.setReferenceFrame(referenceFrame);

        return result;
    }

}
