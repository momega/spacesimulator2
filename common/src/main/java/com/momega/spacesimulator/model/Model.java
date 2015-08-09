package com.momega.spacesimulator.model;

import org.springframework.util.Assert;

import java.sql.Time;
import java.util.*;

/**
 * Created by martin on 7/25/15.
 */
public class Model {

    private List<MovingObject> movingObjects = new ArrayList<>();
    private Map<Timestamp, Map<MovingObject, Instant>> instants = new HashMap<>();

    public List<MovingObject> getMovingObjects() {
        return movingObjects;
    }

    public void setMovingObjects(List<MovingObject> movingObjects) {
        this.movingObjects = movingObjects;
    }

    public Instant getInstant(Timestamp timestamp, MovingObject movingObject) {
        Map<MovingObject, Instant> map = instants.get(timestamp);
        if (map == null) {
            return null;
        }
        return map.get(movingObject);
    }

    public void addInstant(Instant instant) {
        Assert.notNull(instant);
        Assert.notNull(instant.getMovingObject());
        Assert.notNull(instant.getTimestamp());
        Map<MovingObject, Instant> map = instants.get(instant.getTimestamp());
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(instant.getMovingObject(), instant);
    }

    public void removeInstants(Timestamp timestamp) {
        Assert.notNull(timestamp);
        instants.remove(timestamp);
    }
}
