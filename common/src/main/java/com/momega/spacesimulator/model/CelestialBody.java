package com.momega.spacesimulator.model;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;

/**
 * The class represents the celestial such as planet.It is the {@link PhysicalBody} with defined radius and the rotation period.
 * Created by martin on 8/9/15.
 */
public class CelestialBody extends PhysicalBody {

    private double rotationPeriod; // rotation period in seconds
    private double radius;
    private double primeMeridian;
    private double ra;
    private double dec;
    private Rotation axialTilt;

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

    /**
     * Right ascension (RA) of the north pole
     * @return the angle of the right ascension of the north pole
     */
    public double getRa() {
        return ra;
    }

    public void setRa(double ra) {
        this.ra = ra;
    }

    /**
     * Declination of the north pole
     * @return the angle of the declination of the north pole
     */
    public double getDec() {
        return dec;
    }

    public void setDec(double dec) {
        this.dec = dec;
    }

    public Rotation getAxialTilt() {
        return axialTilt;
    }

    public void setAxialTilt(Rotation axialTilt) {
        this.axialTilt = axialTilt;
    }

}
