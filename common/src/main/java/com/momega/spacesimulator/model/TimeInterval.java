package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/16/14.
 */
public class TimeInterval {

    private Timestamp startTime;
    private Timestamp endTime;

    /**
     * Get the start timestamp of the interval
     * @return the timestamp
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * Get the end timestamp of the interval
     * @return the timestamp
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
