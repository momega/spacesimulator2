package com.momega.spacesimulator.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Created by martin on 7/19/15.
 */
public class CartesianState {

    private ReferenceFrame referenceFrame;
    private Vector3D position;
    private Vector3D velocity;
    private Timestamp timestamp;

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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "CartesianState{" +
                "referenceFrame=" + referenceFrame +
                ", position=" + position +
                ", velocity=" + velocity +
                ", timestamp=" + timestamp +
                '}';
    }
}
