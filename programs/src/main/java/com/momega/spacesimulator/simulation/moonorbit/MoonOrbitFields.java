package com.momega.spacesimulator.simulation.moonorbit;

import com.momega.spacesimulator.model.Timestamp;


public class MoonOrbitFields {
	
	private double speed;
	private double burnTime;

	private Timestamp startTime;
	private Timestamp endTime;

	private Timestamp startBurnTime;
	private Timestamp endBurnTime;

	private double stepTime;
	
	private double apolune;
	private double perilune;
	private double maxEccentricity;

    public Timestamp getEndBurnTime() {
        return endBurnTime;
    }

    public void setEndBurnTime(Timestamp endBurnTime) {
        this.endBurnTime = endBurnTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Timestamp getStartBurnTime() {
        return startBurnTime;
    }

    public void setStartBurnTime(Timestamp startBurnTime) {
        this.startBurnTime = startBurnTime;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public double getStepTime() {
        return stepTime;
    }

    public void setStepTime(double stepTime) {
        this.stepTime = stepTime;
    }

    public double getBurnTime() {
        return burnTime;
    }

    public void setBurnTime(double burnTime) {
        this.burnTime = burnTime;
    }
    
    public void setApolune(double apolune) {
		this.apolune = apolune;
	}
    
    public double getApolune() {
		return apolune;
	}
    
    public void setPerilune(double perilune) {
		this.perilune = perilune;
	}
    
    public double getPerilune() {
		return perilune;
	}
    
    public void setMaxEccentricity(double maxEccentricity) {
		this.maxEccentricity = maxEccentricity;
	}
    
    public double getMaxEccentricity() {
		return maxEccentricity;
	}

    @Override
    public String toString() {
        return "MoonOrbitParameters{" +
                "speed=" + speed +
                ", burnTime=" + burnTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", startBurnTime=" + startBurnTime +
                ", endBurnTime=" + endBurnTime +
                ", stepTime=" + stepTime +
                '}';
    }
}
