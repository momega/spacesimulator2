package com.momega.spacesimulator.service.propagator;

import java.util.Map;

import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Timestamp;

/**
 * Created by martin on 8/11/15.
 */
public class PropagationResult {
    private Timestamp startTime;
    private Timestamp endTime;
    private long execTime;
    private Map<MovingObject, Instant> instants;

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

    public long getExecTime() {
        return execTime;
    }

    public void setExecTime(long execTime) {
        this.execTime = execTime;
    }

    public void setInstants(Map<MovingObject, Instant> instants) {
        this.instants = instants;
    }

    public Map<MovingObject, Instant> getInstants() {
        return instants;
    }
}
