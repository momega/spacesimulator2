package com.momega.spacesimulator.model;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The model is the POJO object containing all the data. It contains definition of moving objects and all instances of history points {@link Instant}.
 * Created by martin on 5/6/14.
 */
public class Model {

    private List<MovingObject> movingObjects = new ArrayList<>();
    private Map<Timestamp, Map<MovingObject, Instant>> instants = new HashMap<>();
    private ReferenceFrame rootReferenceFrame;

    public List<MovingObject> getMovingObjects() {
        return movingObjects;
    }

    public void setMovingObjects(List<MovingObject> movingObjects) {
        this.movingObjects = movingObjects;
    }

    public void addInstant(Instant instant) {
        Assert.notNull(instant);
        Assert.notNull(instant.getMovingObject());
        Assert.notNull(instant.getTimestamp());
        Map<MovingObject, Instant> map = instants.get(instant.getTimestamp());
        if (map == null) {
            map = new HashMap<>();
            instants.put(instant.getTimestamp(), map);
        }
        map.put(instant.getMovingObject(), instant);
    }

    public void removeInstants(Timestamp timestamp) {
        Assert.notNull(timestamp);
        instants.remove(timestamp);
    }

    public void setRootReferenceFrame(ReferenceFrame rootReferenceFrame) {
        this.rootReferenceFrame = rootReferenceFrame;
    }

    public ReferenceFrame getRootReferenceFrame() {
        return rootReferenceFrame;
    }

    public Map<Timestamp, Map<MovingObject, Instant>> getInstants() {
        return instants;
    }
}
