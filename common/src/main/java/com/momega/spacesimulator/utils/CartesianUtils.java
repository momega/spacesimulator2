package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.ReferenceFrame;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.springframework.stereotype.Component;

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

    public CartesianState add(CartesianState first, CartesianState other) {
        boolean theSameReferaceFrame = (first.getReferenceFrame() == other.getReferenceFrame());
        if (theSameReferaceFrame == false) {
            throw new IllegalArgumentException("cartesian states are not within the same reference frame");
        }
        CartesianState result = new CartesianState();
        result.setPosition(first.getPosition().add(other.getPosition()));
        result.setVelocity(first.getVelocity().add(other.getVelocity()));
        return result;
    }

    public CartesianState subtract(CartesianState first, CartesianState other) {
        boolean theSameReferaceFrame = (first.getReferenceFrame() == other.getReferenceFrame());
        if (theSameReferaceFrame == false) {
            throw new IllegalArgumentException("cartesian states are not within the same reference frame");
        }
        CartesianState result = new CartesianState();
        result.setPosition(first.getPosition().subtract(other.getPosition()));
        result.setVelocity(first.getVelocity().subtract(other.getVelocity()));
        return result;
    }
}
