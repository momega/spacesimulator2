/**
 * 
 */
package com.momega.spacesimulator.simulation.voyagetomoon;

import com.momega.spacesimulator.model.Timestamp;

/**
 * @author martin
 *
 */
public class VoyageToMoonFields {

	private Timestamp startTime;
	private Timestamp endTime;
	private double startSpeed;
	private double endSpeed;
	private double speedStep;
	private double stepInSeconds;
	private double minSurface;
	private double maxSurface;
	private double maxEccentricity;
	
	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public double getStartSpeed() {
		return startSpeed;
	}

	public void setStartSpeed(double startSpeed) {
		this.startSpeed = startSpeed;
	}

	public double getEndSpeed() {
		return endSpeed;
	}

	public void setEndSpeed(double endSpeed) {
		this.endSpeed = endSpeed;
	}

	public double getStepInSeconds() {
		return stepInSeconds;
	}

	public void setStepInSeconds(double stepInSeconds) {
		this.stepInSeconds = stepInSeconds;
	}
	
	public void setMaxSurface(double maxSurface) {
		this.maxSurface = maxSurface;
	}
	
	public double getMaxSurface() {
		return maxSurface;
	}
	
	public void setMinSurface(double minSurface) {
		this.minSurface = minSurface;
	}
	
	public double getMinSurface() {
		return minSurface;
	}
	
	public double getMaxEccentricity() {
		return maxEccentricity;
	}
	
	public void setMaxEccentricity(double maxEccentricity) {
		this.maxEccentricity = maxEccentricity;
	}
	
	public void setSpeedStep(double speedStep) {
		this.speedStep = speedStep;
	}
	
	public double getSpeedStep() {
		return speedStep;
	}

	@Override
	public String toString() {
		return "VoyageToMoonFields [startTime=" + startTime + ", endTime="
				+ endTime + ", startSpeed=" + startSpeed + ", endSpeed="
				+ endSpeed + ", speedStep=" + speedStep + ", stepInSeconds="
				+ stepInSeconds + ", minSurface=" + minSurface
				+ ", maxSurface=" + maxSurface + ", maxEccentricity="
				+ maxEccentricity + "]";
	}

}
