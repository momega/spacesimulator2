package com.momega.spacesimulator.model;

/**
 * Defines the maneuver of the spacecraft
 */
public class Maneuver {

    private double throttle;
    private double throttleAlpha;
    private double throttleDelta;
    private boolean inverse;
    private TimeInterval interval;
    private ReferenceFrameDefinition referenceFrameDefinition; 

    public double getThrottle() {
        return throttle;
    }

    public void setThrottle(double throttle) {
        this.throttle = throttle;
    }

    public double getThrottleAlpha() {
        return throttleAlpha;
    }

    public void setThrottleAlpha(double throttleAlpha) {
        this.throttleAlpha = throttleAlpha;
    }

    public double getThrottleDelta() {
        return throttleDelta;
    }

    public void setThrottleDelta(double throttleDelta) {
        this.throttleDelta = throttleDelta;
    }

    public TimeInterval getInterval() {
        return interval;
    }

    public void setInterval(TimeInterval interval) {
        this.interval = interval;
    }
    
    public void setReferenceFrameDefinition(ReferenceFrameDefinition referenceFrameDefinition) {
		this.referenceFrameDefinition = referenceFrameDefinition;
	}
    
    public ReferenceFrameDefinition getReferenceFrameDefinition() {
		return referenceFrameDefinition;
	}
    
    public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}
    
    public boolean isInverse() {
		return inverse;
	}
}
