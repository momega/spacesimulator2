package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.TimeUtils;

/**
 * Created by martin on 7/19/15.
 */
public class KeplerianOrbit {

    private ReferenceFrame referenceFrame;
    private double semimajorAxis; // (a)
    private double eccentricity; // epsilon
    private double argumentOfPeriapsis; // lowercase omega
    private double inclination; // i
    private double ascendingNode; // uppercase omega
    private Timestamp timeOfPeriapsis; // seconds
    private double period; // in seconds
    private double meanMotion; // n

    public ReferenceFrame getReferenceFrame() {
        return referenceFrame;
    }

    public void setReferenceFrame(ReferenceFrame referenceFrame) {
        this.referenceFrame = referenceFrame;
    }

    public double getSemimajorAxis() {
        return semimajorAxis;
    }

    public void setSemimajorAxis(double semimajorAxis) {
        this.semimajorAxis = semimajorAxis;
    }

    public double getEccentricity() {
        return eccentricity;
    }

    public void setEccentricity(double eccentricity) {
        this.eccentricity = eccentricity;
    }

    public double getArgumentOfPeriapsis() {
        return argumentOfPeriapsis;
    }

    public void setArgumentOfPeriapsis(double argumentOfPeriapsis) {
        this.argumentOfPeriapsis = argumentOfPeriapsis;
    }

    public double getInclination() {
        return inclination;
    }

    public void setInclination(double inclination) {
        this.inclination = inclination;
    }

    public double getAscendingNode() {
        return ascendingNode;
    }

    public void setAscendingNode(double ascendingNode) {
        this.ascendingNode = ascendingNode;
    }

    public Timestamp getTimeOfPeriapsis() {
        return timeOfPeriapsis;
    }

    public void setTimeOfPeriapsis(Timestamp timeOfPeriapsis) {
        this.timeOfPeriapsis = timeOfPeriapsis;
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
    }

    public double getMeanMotion() {
        return meanMotion;
    }

    public void setMeanMotion(double meanMotion) {
        this.meanMotion = meanMotion;
    }

    public boolean isHyperbolic() {
        return (getEccentricity()>1);
    }

    @Override
    public String toString() {
        String result = String.format("(a=%6.2f, e=%6.2f, omega=%6.2f, i=%6.2f, OMEGA=%6.2f, Tp=%s)", semimajorAxis, eccentricity, argumentOfPeriapsis, inclination, ascendingNode, TimeUtils.timeAsString(timeOfPeriapsis));
        return result;
    }

}
