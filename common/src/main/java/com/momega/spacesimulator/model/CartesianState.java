package com.momega.spacesimulator.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Cartesian state of any object. It consists of position and velocity.
 * Created by martin on 7/19/15.
 */
public class CartesianState {

    public CartesianState() {
        super();
    }

    private ReferenceFrame referenceFrame;
    private Vector3D position;
    private Vector3D velocity;

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3D velocity) {
        this.velocity = velocity;
    }

    public ReferenceFrame getReferenceFrame() {
        return referenceFrame;
    }

    public void setReferenceFrame(ReferenceFrame referenceFrame) {
        this.referenceFrame = referenceFrame;
    }

    @Override
    public String toString() {
        return "CartesianState{" +
                "referenceFrame=" + referenceFrame +
                ", position=" + position +
                ", velocity=" + velocity +
                '}';
    }
}
