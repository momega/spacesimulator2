package com.momega.spacesimulator.model;

/**
 * Defines the maneuver of the spacecraft
 */
public class Maneuver {

    private double throttle;
    private double throttleAlpha;
    private double throttleDelta;
    private TimeInterval interval;

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
}
