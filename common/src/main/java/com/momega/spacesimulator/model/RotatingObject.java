package com.momega.spacesimulator.model;

/**
 * The rotation object is the {@link PhysicalBody} with defined radius and the rotation period. The {@link CelestialBody}
 * is the typical subclass of the rotation object.
 * Created by martin on 8/9/15.
 */
public class RotatingObject extends PhysicalBody {

    private double radius;

    /**
     * Gets the radius in meters of the planet
     * @return the value of the radius
     */
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
