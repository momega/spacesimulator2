package com.momega.spacesimulator.model;

import java.sql.Time;

/**
 * Created by martin on 7/19/15.
 */
public class KeplerianElements {

    private KeplerianOrbit keplerianOrbit;
    private double trueAnomaly; // theta
    private Double hyperbolicAnomaly; // HA
    private Double eccentricAnomaly; //EA
    private Timestamp timestamp;

    public KeplerianOrbit getKeplerianOrbit() {
        return keplerianOrbit;
    }

    public void setKeplerianOrbit(KeplerianOrbit keplerianOrbit) {
        this.keplerianOrbit = keplerianOrbit;
    }

    public double getTrueAnomaly() {
        return trueAnomaly;
    }

    public void setTrueAnomaly(double trueAnomaly) {
        this.trueAnomaly = trueAnomaly;
    }

    public Double getHyperbolicAnomaly() {
        return hyperbolicAnomaly;
    }

    public void setHyperbolicAnomaly(Double hyperbolicAnomaly) {
        this.hyperbolicAnomaly = hyperbolicAnomaly;
    }

    public Double getEccentricAnomaly() {
        return eccentricAnomaly;
    }

    public void setEccentricAnomaly(Double eccentricAnomaly) {
        this.eccentricAnomaly = eccentricAnomaly;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "KeplerianElements [keplerianOrbit=" + keplerianOrbit
                + ", trueAnomaly=" + trueAnomaly + "]";
    }
}
