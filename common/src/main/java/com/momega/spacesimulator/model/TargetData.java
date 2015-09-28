package com.momega.spacesimulator.model;

/**
 * Created by martin on 9/28/15.
 */
public class TargetData {

    private CartesianState cartesianState;
    private double planesAngle;
    private double centreAngle;

    public CartesianState getCartesianState() {
        return cartesianState;
    }

    public void setCartesianState(CartesianState cartesianState) {
        this.cartesianState = cartesianState;
    }

    public double getPlanesAngle() {
        return planesAngle;
    }

    public void setPlanesAngle(double planesAngle) {
        this.planesAngle = planesAngle;
    }

    public double getCentreAngle() {
        return centreAngle;
    }

    public void setCentreAngle(double centreAngle) {
        this.centreAngle = centreAngle;
    }

    @Override
    public String toString() {
        return "TargetData{" +
                "cartesianState=" + cartesianState +
                ", planesAngle=" + planesAngle +
                ", centreAngle=" + centreAngle +
                '}';
    }
}
