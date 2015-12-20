/**
 * 
 */
package com.momega.spacesimulator.simulation.moonorbit;

import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.utils.TimeUtils;


/**
 * @author martin
 *
 */
public class MoonOrbitResult {
	
	public Timestamp timestamp;
	public double speed;
	public Timestamp startBurnTime;
	public double burnTime;
	
	public double period;
	public double eccentricity;
	public double perilune;
	public double velocity;
	public double apolune;
	public KeplerianElements ke;
	
	@Override
	public String toString() {
		return "MoonOrbitResult [timestamp=" + TimeUtils.timeAsString(timestamp) + ", speed=" + speed
				+ ", startBurnTime=" + TimeUtils.timeAsString(startBurnTime) + ", burnTime=" + burnTime
				+ ", perilune=" + perilune + ", period=" + period
				+ ", velocity=" + velocity + ", eccentricity=" + eccentricity
				+ ", apolune=" + apolune  + ", KE=" + ke + "]";
	}
	
	

}
