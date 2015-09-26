package com.momega.spacesimulator.propagator;

import com.momega.spacesimulator.common.CoordinateModels;
import com.momega.spacesimulator.dynamic.ReferenceFrameFactory;
import com.momega.spacesimulator.utils.KeplerianUtils;
import com.momega.spacesimulator.dynamic.InstantManager;
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

    @Autowired
    private InstantManager instantManager;

    @Autowired
    private RotationPropagator rotationPropagator;

    @Autowired
    private ReferenceFrameFactory referenceFrameFactory;

    public Instant get(Model model, KeplerianObject keplerianObject, Timestamp newTimestamp) {
        Instant result = instantManager.getInstant(model, keplerianObject, newTimestamp);
        if (result == null) {
            result = compute(model, keplerianObject, newTimestamp);
        }
        return result;
    }

    public Instant compute(Model model, KeplerianObject keplerianObject, Timestamp newTimestamp) {
        KeplerianOrbit keplerianOrbit = keplerianObject.getKeplerianOrbit();
        if (keplerianOrbit == null) {
            ReferenceFrameDefinition rfDef = model.getRootReferenceFrameDefinition();
            ReferenceFrame referenceFrame = referenceFrameFactory.getFrame(rfDef, model, newTimestamp);
            Instant instant = instantManager.newZeroInstance(model, keplerianObject, referenceFrame, newTimestamp);
            return instant;
        }

        Instant newInstant = computeFromOrbit(model, keplerianObject, keplerianOrbit, newTimestamp);
        if (keplerianObject instanceof CelestialBody) {
            rotationPropagator.compute((CelestialBody) keplerianObject, newInstant, newTimestamp);
        }
        return newInstant;
    }

    public Instant computeFromOrbit(Model model, MovingObject movingObject, KeplerianOrbit keplerianOrbit, Timestamp newTimestamp) {
        KeplerianElements keplerianElements = keplerianUtils.fromTimestamp(keplerianOrbit, newTimestamp);
        CartesianState cartesianState = coordinateModels.transform(model, newTimestamp, keplerianElements);

        Instant newInstant = instantManager.newInstant(model, movingObject, cartesianState, keplerianElements, newTimestamp);
        return newInstant;
    }
}
