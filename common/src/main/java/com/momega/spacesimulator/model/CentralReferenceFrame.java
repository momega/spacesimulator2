package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/9/15.
 */
public class CentralReferenceFrame implements ReferenceFrame {

    private CartesianState cartesianState;
    private CelestialBody celestialBody;

    public void setCelestialBody(CelestialBody celestialBody) {
        this.celestialBody = celestialBody;
    }

    public void setCartesianState(CartesianState cartesianState) {
        this.cartesianState = cartesianState;
    }

    public CelestialBody getCelestialBody() {
        return celestialBody;
    }

    @Override
    public ReferenceFrame getParent() {
        return null;
    }

    @Override
    public CartesianState getCartesianState() {
        return cartesianState;
    }

    @Override
    public double getGravitationParameter() {
        return celestialBody.getGravitationParameter();
    }
}
