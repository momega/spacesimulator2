package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/9/15.
 */
public class CelestialBodyReferenceFrame implements ReferenceFrame {

    private CelestialBody celestialBody;

    @Override
    public ReferenceFrame getParent() {
        //TODO: implement this
        return null;
    }

    @Override
    public CartesianState getCartesianState() {
        return null;
    }

    @Override
    public double getGravitationParameter() {
        return getCelestialBody().getGravitationParameter();
    }

    public CelestialBody getCelestialBody() {
        return celestialBody;
    }

    public void setCelestialBody(CelestialBody celestialBody) {
        this.celestialBody = celestialBody;
    }
}
