package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.ReferenceFrame;
import com.momega.spacesimulator.model.ReferenceFrameDefinition;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by martin on 9/2/15.
 */
@Component
public class CartesianUtils {

    public CartesianState zero() {
        CartesianState cartesianState = new CartesianState();
        cartesianState.setPosition(Vector3D.ZERO);
        cartesianState.setVelocity(Vector3D.ZERO);
        return cartesianState;
    }

    private CartesianState add(CartesianState first, CartesianState other) {
        CartesianState result = new CartesianState();
        result.setPosition(first.getPosition().add(other.getPosition()));
        result.setVelocity(first.getVelocity().add(other.getVelocity()));
        result.setReferenceFrame(first.getReferenceFrame());
        return result;
    }

    public CartesianState subtract(CartesianState first, CartesianState other) {
        ReferenceFrameDefinition firstDef = first.getReferenceFrame().getDefinition();
        ReferenceFrameDefinition otherDef = other.getReferenceFrame().getDefinition();
        if (firstDef != otherDef) {
            throw new IllegalStateException("incompatible Carstesian states");
        }
        CartesianState result = new CartesianState();
        result.setPosition(first.getPosition().subtract(other.getPosition()));
        result.setVelocity(first.getVelocity().subtract(other.getVelocity()));
        result.setReferenceFrame(first.getReferenceFrame());
        return result;
    }

    public CartesianState transferToParent(CartesianState cartesianState) {
        Assert.notNull(cartesianState);
        CartesianState result = new CartesianState();
        result.setPosition(cartesianState.getPosition().add(cartesianState.getReferenceFrame().getCartesianState().getPosition()));
        result.setVelocity(cartesianState.getVelocity().add(cartesianState.getReferenceFrame().getCartesianState().getVelocity()));
        result.setReferenceFrame(cartesianState.getReferenceFrame().getParent());
        return result;
    }

    public CartesianState transferToRoot(CartesianState cartesianState) {
        ReferenceFrame rf = cartesianState.getReferenceFrame();
        CartesianState result = cartesianState;
        while (rf != null) {
            result = transferToParent(result);
            rf = result.getReferenceFrame();
        }
        return result;
    }

    /**
     * Computes angular momentum
     * @return the angular momentum
     */
    public Vector3D getAngularMomentum(CartesianState cartesianState) {
        return cartesianState.getPosition().crossProduct(cartesianState.getVelocity());
    }

    private static final double DEFAULT_TOLERANCE = 1.0e-10;

    protected Plane createOrbitalPlane(Instant instant) {
        CartesianState relative = instant.getCartesianState();
        Vector3D normal = getAngularMomentum(relative);
        Vector3D origin = Vector3D.ZERO;
        return new Plane(origin, normal, DEFAULT_TOLERANCE);
    }
}
