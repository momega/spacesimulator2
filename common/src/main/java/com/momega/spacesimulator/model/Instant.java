package com.momega.spacesimulator.model;

/**
 * The instant class represents single instance of the moving object in time.
 * Created by martin on 7/21/15.
 */
public class Instant {

    private CartesianState cartesianState;
    private KeplerianElements keplerianElements;
    private Timestamp timestamp;
    private MovingObject movingObject;

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
}
