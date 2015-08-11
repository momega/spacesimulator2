package com.momega.spacesimulator.dynamic;

import com.momega.spacesimulator.model.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by martin on 7/22/15.
 */
@Component
public class InstantManager {

    public Map<MovingObject, Instant> getInstants(Model model, Timestamp timestamp) {
        Map<MovingObject, Instant> map = model.getInstants().get(timestamp);
        return map;
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

        Instant instant = new Instant();
        instant.setCartesianState(CartesianState.getZero(referenceFrame));
        instant.setKeplerianElements(null);
        instant.setMovingObject(movingObject);
        instant.setTimestamp(timestamp);
        model.addInstant(instant);
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
        model.addInstant(instant);
        return instant;
    }
}
