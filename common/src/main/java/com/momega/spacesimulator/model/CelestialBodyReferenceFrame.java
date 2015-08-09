package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/9/15.
 */
public class CelestialBodyReferenceFrame implements ReferenceFrame {

    private Instant instant;

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    @Override
    public ReferenceFrame getParent() {
        //TODO: implement this
        return null;
    }

    @Override
    public CartesianState getCartesianState() {
        return instant.getCartesianState();
    }

    @Override
    public double getGravitationParameter() {
        return getCelestalBody().getGravitationParameter();
    }

    private CelestialBody getCelestalBody() {
        return (CelestialBody) getInstant().getMovingObject();
    }
}
