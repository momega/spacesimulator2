package com.momega.spacesimulator.propagator.force;

import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.service.ManeuverService;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.RotationUtils;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 11/1/15.
 */
@Component
public class ThrustModel implements ForceModel {
	
	private static final Logger logger = LoggerFactory.getLogger(ThrustModel.class);

    @Autowired
    private RotationUtils rotationUtils;

    @Autowired
    private InstantManager instantManager;

    @Autowired
    private ManeuverService maneuverService;

    public AccelerationResult getAcceleration(Model model, Spacecraft spacecraft, CartesianState currentState, Timestamp timestamp, double dt) {
        Instant instant = instantManager.getInstant(model, spacecraft, timestamp);
        SpacecraftState spacecraftState = instant.getSpacecraftState();
        Vector3D velocity = instant.getCartesianState().getVelocity();

        AccelerationResult result = new AccelerationResult();

        Propulsion propulsion = spacecraft.getPropulsion();
        if (propulsion  == null) {
            result.setSpacecraftState(spacecraftState);
            result.setAcceleration(Vector3D.ZERO);
            return result;
        }

        Maneuver maneuver = maneuverService.findManeuver(spacecraft, timestamp);
        if (maneuver == null) {
            result.setSpacecraftState(spacecraftState);
            result.setAcceleration(Vector3D.ZERO);
            return result;
        }

        double dm = propulsion.getMassFlow() * maneuver.getThrottle() * dt;
        double thrust = propulsion.getMassFlow() * maneuver.getThrottle() * propulsion.getSpecificImpulse() * MathUtils.G0;
        double a = thrust / spacecraft.getInitialMass();

        SpacecraftState newSpacecraftState = new SpacecraftState();
        newSpacecraftState.setMass(spacecraftState.getMass() - dm);
        newSpacecraftState.setFuel(spacecraftState.getFuel() - dm);
        newSpacecraftState.setEngineActived(true);

        Rotation r = new Rotation(RotationOrder.XZY, 0, maneuver.getThrottleAlpha(), maneuver.getThrottleDelta());

        Vector3D acceleration = r.applyTo(velocity.normalize()).scalarMultiply(a);
        
        logger.debug("burst at {} with acceleration {}", timestamp, acceleration);

        result.setSpacecraftState(newSpacecraftState);
        result.setAcceleration(acceleration);
        
        return result;
    }
}
