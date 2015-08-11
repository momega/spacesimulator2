package com.momega.spacesimulator.propagator;

import com.momega.spacesimulator.common.CoordinateModels;
import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.propagator.model.GravityModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.sql.Time;

/**
 * The propagator computes new position of the spacecraft. It uses newtonian gravitation model
 * Created by martin on 7/21/15.
 */
@Component
public class NewtonianPropagator {

    @Autowired
    @SuppressWarnings("null")
    private GravityModel gravityModel;

    @Autowired
    private CoordinateModels coordinateModels;

    @Autowired
    private InstantManager instantManager;


    public Instant compute(Model model, MovingObject movingObject, Timestamp timestamp, Timestamp newTimestamp, double dt) {
        Instant instant = instantManager.getInstant(model, movingObject, timestamp);
        Assert.notNull(instant);

        CartesianState cartesianState = rk4Solver(model, instant, timestamp, dt);
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
    protected CartesianState eulerSolver(Model model, Instant spacecraft, Timestamp timestamp, double dt) {
        // Euler's method
        Vector3D position = spacecraft.getCartesianState().getPosition();
        Vector3D velocity = spacecraft.getCartesianState().getVelocity();
        ReferenceFrame referenceFrame = spacecraft.getCartesianState().getReferenceFrame();

        // iterate all force models
        Vector3D acceleration = getAcceleration(model, position, timestamp);

        velocity = velocity.add(dt, acceleration); // velocity: v(i) = v(i) + a(i) * dt
        position = position.add(dt, velocity); // position: r(i) = r(i) * v(i) * dt
        // cartesian state
        CartesianState result = new CartesianState();
        result.setVelocity(velocity);
        result.setPosition(position);
        result.setReferenceFrame(referenceFrame);

        return result;
    }

    protected Vector3D getAcceleration(Model model, Vector3D position, Timestamp timestamp) {
        return gravityModel.getAcceleration(model, position, timestamp);
    }

    /**
     * Solves the velocity and position by RK4 method (Runge-Kutta method, 4th order)
     * @param spacecraft the spacecraft instance
     * @param timestamp the timestamp
     * @param dt time interval
     * @return new position
     */
    protected CartesianState rk4Solver(Model model, Instant spacecraft, Timestamp timestamp, double dt) {
        Vector3D position = spacecraft.getCartesianState().getPosition();
        Vector3D velocity = spacecraft.getCartesianState().getVelocity();
        ReferenceFrame referenceFrame = spacecraft.getCartesianState().getReferenceFrame();

        // k[i]v are velocities
        // k[i]x are position

        Timestamp halfTime = timestamp.add(dt/2);
        Timestamp newTime = timestamp.add(dt);

        Vector3D k1v = getAcceleration(model, position, timestamp).scalarMultiply(dt);
        Vector3D k1x = velocity.scalarMultiply(dt);
        Vector3D k2v = getAcceleration(model, position.add(dt/2, k1x), halfTime).scalarMultiply(dt);
        Vector3D k2x = velocity.add(1.0/2, k1v).scalarMultiply(dt);
        Vector3D k3v = getAcceleration(model, position.add(dt/2, k2x), halfTime).scalarMultiply(dt);
        Vector3D k3x = velocity.add(1.0/2, k2v).scalarMultiply(dt);
        Vector3D k4v = getAcceleration(model, position.add(dt, k3x), newTime).scalarMultiply(dt);
        Vector3D k4x = velocity.add(1.0, k3v).scalarMultiply(dt);

        velocity = velocity.add(rk4(k1v, k2v, k3v, k4v));
        position = position.add(rk4(k1x, k2x, k3x, k4x));

        // cartesian state
        CartesianState result = new CartesianState();
        result.setVelocity(velocity);
        result.setPosition(position);
        result.setReferenceFrame(referenceFrame);
        return result;
    }

    protected Vector3D rk4(Vector3D u1, Vector3D u2, Vector3D u3, Vector3D u4) {
        return u1.add(2, u2).add(2, u3).add(u4).scalarMultiply(1.0 / 6);
    }

}
