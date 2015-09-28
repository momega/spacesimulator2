package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/9/15.
 */
public class Spacecraft extends MovingObject {

    private CelestialBody target;

    public CelestialBody getTarget() {
        return target;
    }

    public void setTarget(CelestialBody target) {
        this.target = target;
    }
}
