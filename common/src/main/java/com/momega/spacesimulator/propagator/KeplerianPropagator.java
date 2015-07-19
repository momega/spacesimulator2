package com.momega.spacesimulator.propagator;

import com.momega.spacesimulator.common.CoordinateModels;
import com.momega.spacesimulator.common.KeplerianUtils;
import com.momega.spacesimulator.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 7/19/15.
 */
@Component
public class KeplerianPropagator {

    @Autowired
    private KeplerianUtils keplerianUtils;

    @Autowired
    private CoordinateModels coordinateModels;

    public void compute(MovingObject movingObject, Timestamp timestamp) {
        KeplerianOrbit keplerianOrbit = movingObject.getKeplerianElements().getKeplerianOrbit();

        KeplerianElements keplerianElements = keplerianUtils.fromTimestamp(keplerianOrbit, timestamp);
        CartesianState cartesianState = coordinateModels.transform(keplerianElements);

        movingObject.setKeplerianElements(keplerianElements);
        movingObject.setCartesianState(cartesianState);
    }
}
