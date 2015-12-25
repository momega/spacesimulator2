/**
 * 
 */
package com.momega.spacesimulator.simulation.moonorbit;

import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.utils.TimeUtils;


/**
 * @author martin
 *
 */
public class MoonOrbitResult {
	
	// input
	public Timestamp timestamp;
	public double speed;
	public Timestamp startBurnTime;
	public double burnTime;
	
	// output
	public double period;
	public double eccentricity;
	public double perilune;
	public double velocity;
	public double apolune;
	
	@Override
	public String toString() {
		return "MoonOrbitResult [timestamp=" + TimeUtils.timeAsString(timestamp) + ", speed=" + speed
				+ ", startBurnTime=" + TimeUtils.timeAsString(startBurnTime) + ", burnTime=" + burnTime
				+ ", perilune=" + perilune/1000 + "km, period=" + period
				+ ", velocity=" + velocity + ", eccentricity=" + eccentricity
				+ ", apolune=" + apolune/1000 + "km]";
	}
	
	

}
