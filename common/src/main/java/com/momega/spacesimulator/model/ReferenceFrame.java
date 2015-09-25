package com.momega.spacesimulator.model;

/**
 * Created by martin on 7/19/15.
 */
public class ReferenceFrame {

    private double gravitationParameter;
    private ReferenceFrame parent;
    private CartesianState cartesianState;
    private ReferenceFrameDefinition definition;
    private Timestamp timestamp;

    public ReferenceFrame getParent() {
        return parent;
    }

    public void setParent(ReferenceFrame parent) {
        this.parent = parent;
    }

    public CartesianState getCartesianState() {
        return cartesianState;
    }

    public void setCartesianState(CartesianState cartesianState) {
        this.cartesianState = cartesianState;
    }

    public double getGravitationParameter() {
        return gravitationParameter;
    }

    public void setGravitationParameter(double gravitationParameter) {
        this.gravitationParameter = gravitationParameter;
    }

    public void setDefinition(ReferenceFrameDefinition definition) {
        this.definition = definition;
    }

    public ReferenceFrameDefinition getDefinition() {
        return definition;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "ReferenceFrame{" +
                " definition=" + definition + "}";
    }

}
