/**
 * 
 */
package com.momega.spacesimulator.simulation.voyagetomoon;

import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * @author martin
 *
 */
public class VoyageToMoonResult {
	
	// input
	public Timestamp timestamp;
	public double speed;

	// output
	public KeplerianElements keplerianElements;
	public double distance;
	public double velocity;
	public Timestamp minTimestamp;
	public double duration;
	public double surface;
	
	@Override
	public String toString() {
		return "VoyageToMoonResult [timestamp=" + TimeUtils.timeAsString(timestamp) + ", speed="
				+ speed + ", surface=" + surface + ", keplerianElements="
				+ keplerianElements + ", velocity=" + velocity
				+ ", minTimestamp=" + TimeUtils.timeAsString(minTimestamp) + ", duration=" + TimeUtils.durationAsString(duration)
				+ ", distance=" + distance + "]";
	}
	
}
