package com.momega.spacesimulator.service.propagator.force;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Propulsion;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.SpacecraftState;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.InstantManager;
import com.momega.spacesimulator.service.ManeuverService;
import com.momega.spacesimulator.service.utils.CartesianUtils;
import com.momega.spacesimulator.service.utils.MathUtils;
import com.momega.spacesimulator.service.utils.RotationUtils;
import com.momega.spacesimulator.service.utils.TimeUtils;

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
    private CartesianUtils cartesianUtils;

    @Autowired
    private ManeuverService maneuverService;

    public AccelerationResult getAcceleration(Model model, Spacecraft spacecraft, CartesianState currentState, Timestamp timestamp, double dt) {
        Instant instant = instantManager.getInstant(model, spacecraft, timestamp);
        SpacecraftState spacecraftState = instant.getSpacecraftState();

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
        
        Vector3D velocity = instant.getCartesianState().getVelocity();
        
        logger.debug("velocity = |{}| or {}", velocity.getNorm(), velocity);

        double dm = propulsion.getMassFlow() * maneuver.getThrottle() * dt;
        double thrust = propulsion.getMassFlow() * maneuver.getThrottle() * propulsion.getSpecificImpulse() * MathUtils.G0;
        double a = thrust / spacecraftState.getMass();

        SpacecraftState newSpacecraftState = new SpacecraftState();
        newSpacecraftState.setMass(spacecraftState.getMass() - dm);
        newSpacecraftState.setFuel(spacecraftState.getFuel() - dm);
        newSpacecraftState.setEngineActived(true);

        Vector3D acceleration;
        if (maneuver.isInverse()) {
        	acceleration = velocity.negate().normalize().scalarMultiply(a);
        } else {
        	Rotation r = new Rotation(RotationOrder.ZXZ, 0, maneuver.getThrottleAlpha(), maneuver.getThrottleDelta());
        	acceleration = r.applyTo(velocity.normalize()).scalarMultiply(a);
        }
        
        logger.debug("burst at {} with acceleration |{}| or vector {} ", TimeUtils.timeAsString(timestamp), acceleration.getNorm(), acceleration);

        result.setSpacecraftState(newSpacecraftState);
        result.setAcceleration(acceleration);
        
        return result;
    }
}
