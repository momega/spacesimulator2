package com.momega.spacesimulator.propagator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.dynamic.ReferenceFrameFactory;
import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.KeplerianObject;
import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.ReferenceFrame;
import com.momega.spacesimulator.model.ReferenceFrameDefinition;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.CoordinateService;
import com.momega.spacesimulator.utils.KeplerianUtils;

/**
 * Created by martin on 7/19/15.
 */
@Component
public class KeplerianPropagator {

    @Autowired
    private KeplerianUtils keplerianUtils;

    @Autowired
    private CoordinateService coordinateService;

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
            ReferenceFrameDefinition rfDef = keplerianObject.getReferenceFrameDefinition();
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
        CartesianState cartesianState = coordinateService.transform(model, newTimestamp, keplerianElements);

        Instant newInstant = instantManager.newInstant(model, movingObject, cartesianState, keplerianElements, newTimestamp);
        return newInstant;
    }
}
