package com.momega.spacesimulator.dynamic;

import com.momega.spacesimulator.model.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by martin on 7/22/15.
 */
@Component
public class InstantManager {

    public Instant getInstant(Model model, MovingObject movingObject, Timestamp timestamp) {
        return model.getInstant(timestamp, movingObject);
    }

    public Instant newInstant(Model model, MovingObject movingObject, CartesianState cartesianState, KeplerianElements keplerianElements) {
        Assert.notNull(model);
        Assert.notNull(movingObject);
        Assert.notNull(cartesianState);
        Assert.notNull(keplerianElements);
        Instant instant = new Instant();
        instant.setCartesianState(cartesianState);
        instant.setKeplerianElements(keplerianElements);
        instant.setMovingObject(movingObject);
        instant.setTimestamp(cartesianState.getTimestamp());
        model.addInstant(instant);
        return instant;
    }
}
