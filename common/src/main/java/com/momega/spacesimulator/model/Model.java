package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.buffer.BoundedFifoBuffer;

/**
 * The force is the POJO object containing all the data. It contains definition of moving objects and all instances of history points {@link Instant}.
 * Created by martin on 5/6/14.
 */
public class Model {

    private List<MovingObject> movingObjects = new ArrayList<>();
    private Map<Timestamp, Map<MovingObject, Instant>> instants = new HashMap<>();
    private ReferenceFrameDefinition rootReferenceFrameDefinition;
    private BoundedFifoBuffer timestamps;

    public List<MovingObject> getMovingObjects() {
        return movingObjects;
    }

    public void setMovingObjects(List<MovingObject> movingObjects) {
        this.movingObjects = movingObjects;
    }

    public ReferenceFrameDefinition getRootReferenceFrameDefinition() {
        return rootReferenceFrameDefinition;
    }

    public void setRootReferenceFrameDefinition(ReferenceFrameDefinition rootReferenceFrameDefinition) {
        this.rootReferenceFrameDefinition = rootReferenceFrameDefinition;
    }

    public Map<Timestamp, Map<MovingObject, Instant>> getInstants() {
        return instants;
    }

    public void setInstants(Map<Timestamp, Map<MovingObject, Instant>> instants) {
        this.instants = instants;
    }

    public void setTimestamps(BoundedFifoBuffer timestamps) {
        this.timestamps = timestamps;
    }

    public BoundedFifoBuffer getTimestamps() {
        return timestamps;
    }
}
