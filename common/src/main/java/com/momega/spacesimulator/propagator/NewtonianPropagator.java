package com.momega.spacesimulator.propagator;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.propagator.force.AccelerationResult;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.dynamic.ReferenceFrameFactory;
import com.momega.spacesimulator.propagator.force.GravityModel;
import com.momega.spacesimulator.service.CoordinateService;

/**
 * The propagator computes new position of the spacecraft. It uses newtonian gravitation force
 * Created by martin on 7/21/15.
 */
@Component
public class NewtonianPropagator {
	
	private static final Logger logger = LoggerFactory.getLogger(NewtonianPropagator.class);

    @Autowired
    private GravityModel gravityModel;

    @Autowired
    private CoordinateService coordinateService;

    @Autowired
    private InstantManager instantManager;

    @Autowired
    private ReferenceFrameFactory referenceFrameFactory;


    public Instant compute(Model model, MovingObject movingObject, Timestamp timestamp, Timestamp newTimestamp, double dt) {
        Instant instant = instantManager.getInstant(model, movingObject, timestamp);
        Assert.notNull(instant);

        CartesianState cartesianState = eulerSolver(model, instant, timestamp, dt, newTimestamp);
        KeplerianElements keplerianElements = coordinateService.transform(cartesianState, newTimestamp);
        logger.debug("keplerian elements = {}", keplerianElements);

        Instant newInstant = instantManager.newInstant(model, movingObject, cartesianState, keplerianElements, newTimestamp);
        return newInstant;
    }

    /**
     * Solves the velocity and position by the simple Euler method
     * @param instant the spacecraft instance
     * @param dt time interval
     * @return new cartesian state
     */
    protected CartesianState eulerSolver(Model model, Instant instant, Timestamp timestamp, double dt, Timestamp newTimestamp) {
        // Euler's method
        Vector3D position = instant.getCartesianState().getPosition();
        Vector3D velocity = instant.getCartesianState().getVelocity();
        ReferenceFrame referenceFrame = instant.getCartesianState().getReferenceFrame();
        Spacecraft spacecraft = (Spacecraft) instant.getMovingObject();

        // iterate all force models
        Vector3D acceleration = getAcceleration(model, spacecraft, instant.getCartesianState(), timestamp, dt);

        velocity = velocity.add(dt, acceleration); // velocity: v(i) = v(i) + a(i) * dt
        position = position.add(dt, velocity); // position: r(i) = r(i) * v(i) * dt
        // cartesian state
        CartesianState result = new CartesianState();
        result.setVelocity(velocity);
        result.setPosition(position);

        ReferenceFrameDefinition definition = referenceFrame.getDefinition();
        referenceFrame = referenceFrameFactory.getFrame(definition, model, newTimestamp);
        result.setReferenceFrame(referenceFrame);

        return result;
    }

    /**
     * Solves the velocity and position by RK4 method (Runge-Kutta method, 4th order)
     * @param instant the spacecraft instance
     * @param timestamp the timestamp
     * @param dt time interval
     * @return new position
     */
    protected CartesianState rk4Solver(Model model, Spacecraft spacecraft, Instant instant, Timestamp timestamp, double dt, Timestamp newTimestamp) {
        Vector3D position = instant.getCartesianState().getPosition();
        Vector3D velocity = instant.getCartesianState().getVelocity();
        ReferenceFrame referenceFrame = instant.getCartesianState().getReferenceFrame();

        // k[i]v are velocities
        // k[i]x are position

        ReferenceFrameDefinition definition = referenceFrame.getDefinition();

        Timestamp halfTime = timestamp.add(dt/2);
        ReferenceFrame halfReferenceFrame = referenceFrameFactory.getFrame(definition, model, halfTime);
        ReferenceFrame newReferenceFrame = referenceFrameFactory.getFrame(definition, model, newTimestamp);

        Vector3D k1v = getAcceleration(model, spacecraft, position, velocity, referenceFrame, timestamp, dt/2).scalarMultiply(dt);
        Vector3D k1x = velocity.scalarMultiply(dt);
        Vector3D k2v = getAcceleration(model, spacecraft, position.add(dt/2, k1x), velocity.add(0.5, k1v), halfReferenceFrame, halfTime, dt/2).scalarMultiply(dt);
        Vector3D k2x = velocity.add(1.0/2, k1v).scalarMultiply(dt);
        Vector3D k3v = getAcceleration(model, spacecraft, position.add(dt/2, k2x), velocity.add(0.5, k2v), halfReferenceFrame, halfTime, dt/2).scalarMultiply(dt);
        Vector3D k3x = velocity.add(1.0/2, k2v).scalarMultiply(dt);
        Vector3D k4v = getAcceleration(model, spacecraft, position.add(dt, k3x), velocity.add(k3v), newReferenceFrame, newTimestamp, dt/2).scalarMultiply(dt);
        Vector3D k4x = velocity.add(1.0, k3v).scalarMultiply(dt);

        velocity = velocity.add(rk4(k1v, k2v, k3v, k4v));
        position = position.add(rk4(k1x, k2x, k3x, k4x));

        // cartesian state
        CartesianState result = new CartesianState();
        result.setVelocity(velocity);
        result.setPosition(position);
        result.setReferenceFrame(newReferenceFrame);

        return result;
    }

    protected Vector3D rk4(Vector3D u1, Vector3D u2, Vector3D u3, Vector3D u4) {
        return u1.add(2, u2).add(2, u3).add(u4).scalarMultiply(1.0 / 6);
    }

    protected Vector3D getAcceleration(Model model, Spacecraft spacecraft, CartesianState currentState, Timestamp timestamp, double dt) {
        AccelerationResult accelerationResult = gravityModel.getAcceleration(model, spacecraft, currentState, timestamp, dt);
        return accelerationResult.getAcceleration();
    }

    protected Vector3D getAcceleration(Model model, Spacecraft spacecraft, Vector3D position, Vector3D velocity, ReferenceFrame referenceFrame, Timestamp timestamp, double dt) {
        CartesianState cartesianState = new CartesianState();
        cartesianState.setPosition(position);
        cartesianState.setVelocity(velocity);
        cartesianState.setReferenceFrame(referenceFrame);
        AccelerationResult accelerationResult = gravityModel.getAcceleration(model, spacecraft, cartesianState, timestamp, dt);
        return accelerationResult.getAcceleration();
    }

}
