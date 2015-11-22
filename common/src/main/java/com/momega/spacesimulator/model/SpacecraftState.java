package com.momega.spacesimulator.model;

/**
 * Created by martin on 11/1/15.
 */
public class SpacecraftState {

    private double fuel;
    private double mass;
    private boolean engineActived = false;

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
    
    public boolean isEngineActived() {
		return engineActived;
	}
    
    public void setEngineActived(boolean engineActived) {
		this.engineActived = engineActived;
	}

	@Override
	public String toString() {
		return "SpacecraftState [mass=" + mass + ", fuel=" + fuel
				+ ", engineActived=" + engineActived + "]";
	}
    
}
