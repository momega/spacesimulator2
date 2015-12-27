/**
 * 
 */
package com.momega.spacesimulator.simulation.voyagetomoon;

import com.momega.spacesimulator.model.Timestamp;

/**
 * @author martin
 *
 */
public class VoyageToMoonParameters {

	private Timestamp startTime;
	private Timestamp endTime;
	private int startSpeed;
	private int endSpeed;
	private double stepInSeconds;
	
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

	public int getStartSpeed() {
		return startSpeed;
	}

	public void setStartSpeed(int startSpeed) {
		this.startSpeed = startSpeed;
	}

	public int getEndSpeed() {
		return endSpeed;
	}

	public void setEndSpeed(int endSpeed) {
		this.endSpeed = endSpeed;
	}

	public double getStepInSeconds() {
		return stepInSeconds;
	}

	public void setStepInSeconds(double stepInSeconds) {
		this.stepInSeconds = stepInSeconds;
	}

	@Override
	public String toString() {
		return "VoyageToMoonParameters [startTime=" + startTime + ", endTime="
				+ endTime + ", startSpeed=" + startSpeed + ", endSpeed="
				+ endSpeed + ", stepInSeconds=" + stepInSeconds + "]";
	}

}
