/**
 * 
 */
package com.momega.spacesimulator.runnable;

import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.Timestamp;

/**
 * @author martin
 *
 */
public class VoyageToMoonResult {

	public KeplerianElements keplerianElements;
	public double distance;
	public double velocity;
	public Timestamp timestamp;
	public Timestamp minTimestamp;
	public double speed;
	public double duration;
	public double surface;
	
}
