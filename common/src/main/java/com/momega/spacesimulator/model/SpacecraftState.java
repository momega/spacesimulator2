package com.momega.spacesimulator.model;

/**
 * Created by martin on 11/1/15.
 */
public class SpacecraftState {

    private double fuel;
    private double mass;

    public double getFuel() {
        return fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
}
