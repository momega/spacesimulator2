package com.momega.spacesimulator.model;

/**
 * The rotation object is the {@link PhysicalBody} with defined radius and the rotation period. The {@link CelestialBody}
 * is the typical subclass of the rotation object.
 * Created by martin on 8/9/15.
 */
public class RotatingObject extends PhysicalBody {

    private double rotationPeriod; // rotation period in seconds
    private double radius;
    private double primeMeridian;

    public void setRotationPeriod(double rotationPeriod) {
        this.rotationPeriod = rotationPeriod;
    }

    public double getRotationPeriod() {
        return rotationPeriod;
    }

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

    /**
     * The angle of the prime meridian at epoch JD2000.
     * @return the angle in radians of the prime meridian at epoch JD2000.
     */
    public double getPrimeMeridian() {
        return primeMeridian;
    }

    public void setPrimeMeridian(double primeMeridian) {
        this.primeMeridian = primeMeridian;
    }
}
