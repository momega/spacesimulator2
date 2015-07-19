package com.momega.spacesimulator.model;

/**
 * Created by martin on 7/19/15.
 */
public abstract class MovingObject {

    private String name;

    private CartesianState cartesianState;
    private KeplerianElements keplerianElements;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CartesianState getCartesianState() {
        return cartesianState;
    }

    public void setCartesianState(CartesianState cartesianState) {
        this.cartesianState = cartesianState;
    }

    public KeplerianElements getKeplerianElements() {
        return keplerianElements;
    }

    public void setKeplerianElements(KeplerianElements keplerianElements) {
        this.keplerianElements = keplerianElements;
    }

}
