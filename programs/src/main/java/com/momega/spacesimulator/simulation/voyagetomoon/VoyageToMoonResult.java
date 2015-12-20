/**
 * 
 */
package com.momega.spacesimulator.simulation.voyagetomoon;

import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.utils.TimeUtils;

/**
 * @author martin
 *
 */
public class VoyageToMoonResult {
	
	// input
	private Timestamp timestamp;
	private double speed;

	// output
	private double eccentricity;
	private double distance;
	private double velocity;
	private Timestamp minTimestamp;
	private double duration;
	private double surface;
	
	public Timestamp getTimestamp() {
		return timestamp;
	}



	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}



	public double getSpeed() {
		return speed;
	}



	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getEccentricity() {
		return eccentricity;
	}

	public void setEccentricity(double eccentricity) {
		this.eccentricity = eccentricity;
	}


	public double getDistance() {
		return distance;
	}



	public void setDistance(double distance) {
		this.distance = distance;
	}



	public double getVelocity() {
		return velocity;
	}



	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}



	public Timestamp getMinTimestamp() {
		return minTimestamp;
	}



	public void setMinTimestamp(Timestamp minTimestamp) {
		this.minTimestamp = minTimestamp;
	}



	public double getDuration() {
		return duration;
	}



	public void setDuration(double duration) {
		this.duration = duration;
	}



	public double getSurface() {
		return surface;
	}



	public void setSurface(double surface) {
		this.surface = surface;
	}



	@Override
	public String toString() {
		return "VoyageToMoonResult [timestamp=" + TimeUtils.timeAsString(timestamp) + ", speed="
				+ speed + ", surface=" + surface + ", eccentricity="
				+ eccentricity + ", velocity=" + velocity
				+ ", minTimestamp=" + TimeUtils.timeAsString(minTimestamp) + ", duration=" + TimeUtils.durationAsString(duration)
				+ ", distance=" + distance + "]";
	}
	
}
