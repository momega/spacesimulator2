package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 8/9/15.
 */
public class Spacecraft extends MovingObject {

    private Propulsion propulsion;
    private List<Maneuver> maneuvers = new ArrayList<>();
    private double initialMass;

    private CelestialBody target;
    private Instant minimalInstant;
    private double minimalDistance;
    private double eccentricityThreshold;
    private double threshold;

    public CelestialBody getTarget() {
        return target;
    }

    public void setTarget(CelestialBody target) {
        this.target = target;
    }

    public double getMinimalDistance() {
        return minimalDistance;
    }

    public void setMinimalDistance(double minimalDistance) {
        this.minimalDistance = minimalDistance;
    }

    public Instant getMinimalInstant() {
        return minimalInstant;
    }

    public void setMinimalInstant(Instant minimalInstant) {
        this.minimalInstant = minimalInstant;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }
    
    public void setEccentricityThreshold(double eccentricityThreshold) {
		this.eccentricityThreshold = eccentricityThreshold;
	}
    
    public double getEccentricityThreshold() {
		return eccentricityThreshold;
	}

    public void setPropulsion(Propulsion propulsion) {
        this.propulsion = propulsion;
    }

    public Propulsion getPropulsion() {
        return propulsion;
    }

    public double getInitialMass() {
        return initialMass;
    }

    public void setInitialMass(double initialMass) {
        this.initialMass = initialMass;
    }

    public void setManeuvers(List<Maneuver> maneuvers) {
        this.maneuvers = maneuvers;
    }

    public List<Maneuver> getManeuvers() {
        return maneuvers;
    }
}
