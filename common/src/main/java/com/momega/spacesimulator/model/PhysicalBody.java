package com.momega.spacesimulator.model;

/**
 * Physical body in the space. It is the {@link MovingObject} with the mass and the given orientation.
 * Created by martin on 7/19/15.
 */
public abstract class PhysicalBody extends KeplerianObject {

    private double mass;

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    @Override
    public String toString() {
        return "PhysicalBody{" + super.toString() +
                " mass=" + mass +
                "} ";
    }
}
