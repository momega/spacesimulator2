package com.momega.spacesimulator.dynamic;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.buffer.BoundedFifoBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.ReferenceFrame;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.CartesianUtils;

/**
 * Created by martin on 7/22/15.
 */
@Component
public class InstantManager {

    private int historySize = 10;

    @Autowired
    private CartesianUtils cartesianUtils;

    public Map<MovingObject, Instant> getInstants(Model model, Timestamp timestamp) {
        return model.getInstants().get(timestamp);
    }

    public Instant getInstant(Model model, MovingObject movingObject, Timestamp timestamp) {
        Map<MovingObject, Instant> map = getInstants(model, timestamp);
        if (map == null) {
            return null;
        }
        return map.get(movingObject);
    }

    public Instant newZeroInstance(Model model, MovingObject movingObject, ReferenceFrame referenceFrame, Timestamp timestamp) {
        Assert.notNull(model);
        Assert.notNull(movingObject);
        Assert.notNull(timestamp);

        CartesianState cartesianState = cartesianUtils.zero();
        cartesianState.setReferenceFrame(referenceFrame);

        Instant instant = new Instant();
        instant.setCartesianState(cartesianState);
        instant.setKeplerianElements(null);
        instant.setMovingObject(movingObject);
        instant.setTimestamp(timestamp);
        addInstant(model, instant);
        return instant;
    }
    
    public Instant newInstant(Model model, MovingObject movingObject, Timestamp timestamp) {
        Assert.notNull(model);
        Assert.notNull(movingObject);
        Assert.notNull(timestamp);
        Instant instant = new Instant();
        instant.setMovingObject(movingObject);
        instant.setTimestamp(timestamp);
        addInstant(model, instant);
        return instant;
    }

    public Instant newInstant(Model model, MovingObject movingObject, CartesianState cartesianState, KeplerianElements keplerianElements, Timestamp timestamp) {
        Assert.notNull(model);
        Assert.notNull(movingObject);
        Assert.notNull(cartesianState);
        Assert.notNull(keplerianElements);
        Assert.notNull(timestamp);
        Instant instant = new Instant();
        instant.setCartesianState(cartesianState);
        instant.setKeplerianElements(keplerianElements);
        instant.setMovingObject(movingObject);
        instant.setTimestamp(timestamp);
        addInstant(model, instant);
        return instant;
    }

    private void addInstant(Model model, Instant instant) {
        Assert.notNull(instant);
        Assert.notNull(instant.getMovingObject());
        Assert.notNull(instant.getTimestamp());
        Map<MovingObject, Instant> map = model.getInstants().get(instant.getTimestamp());
        if (map == null) {
            Timestamp time = instant.getTimestamp();
            if (model.getTimestamps()==null) {
                model.setTimestamps(new BoundedFifoBuffer(historySize));
            }
            if (model.getTimestamps().isFull()) {
                Timestamp oldestTimestamp = (Timestamp) model.getTimestamps().remove();
                removeInstants(model, oldestTimestamp);
            }
            map = new HashMap<>();
            model.getInstants().put(instant.getTimestamp(), map);
            model.getTimestamps().add(time);
        }
        map.put(instant.getMovingObject(), instant);
    }

    private void removeInstants(Model model, Timestamp timestamp) {
        Assert.notNull(timestamp);
        model.getInstants().remove(timestamp);
    }

    public void setHistorySize(int historySize) {
        this.historySize = historySize;
    }

}
