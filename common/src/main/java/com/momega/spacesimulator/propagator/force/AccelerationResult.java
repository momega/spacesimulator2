package com.momega.spacesimulator.propagator.force;

import com.momega.spacesimulator.model.SpacecraftState;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Created by martin on 11/7/15.
 */
public class AccelerationResult {

    private Vector3D acceleration;
    private SpacecraftState spacecraftState;

    public Vector3D getAcceleration() {
        return acceleration;
    }

    public void setSpacecraftState(SpacecraftState spacecraftState) {
        this.spacecraftState = spacecraftState;
    }

    public SpacecraftState getSpacecraftState() {
        return spacecraftState;
    }

    public void setAcceleration(Vector3D acceleration) {
        this.acceleration = acceleration;
    }
}
