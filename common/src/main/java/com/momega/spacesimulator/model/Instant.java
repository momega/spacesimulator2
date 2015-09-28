package com.momega.spacesimulator.model;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;

/**
 * The instant class represents single instance of the moving object in time.
 * Created by martin on 7/21/15.
 */
public class Instant {

    private CartesianState cartesianState;
    private KeplerianElements keplerianElements;
    private Timestamp timestamp;
    private MovingObject movingObject;
    private Rotation rotation;
    private TargetData targetData;

    public KeplerianElements getKeplerianElements() {
        return keplerianElements;
    }

    public void setKeplerianElements(KeplerianElements keplerianElements) {
        this.keplerianElements = keplerianElements;
    }

    public CartesianState getCartesianState() {
        return cartesianState;
    }

    public void setCartesianState(CartesianState cartesianState) {
        this.cartesianState = cartesianState;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public MovingObject getMovingObject() {
        return movingObject;
    }

    public void setMovingObject(MovingObject movingObject) {
        this.movingObject = movingObject;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public TargetData getTargetData() {
        return targetData;
    }

    public void setTargetData(TargetData targetData) {
        this.targetData = targetData;
    }

    @Override
    public String toString() {
        return "Instant{" +
                " movingObject=" + movingObject +
                ", timestamp=" + timestamp +
                ", cartesianState=" + cartesianState +
                ", keplerianElements=" + keplerianElements +
                ", rotation=" + rotation +
                ", targetData=" + targetData +
                '}';
    }
}
