package com.momega.spacesimulator.propagator;

import com.momega.spacesimulator.common.CoordinateModels;
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

    public Instant compute(Model model, CelestialBody celestialBody, Timestamp newTimestamp) {
        KeplerianOrbit keplerianOrbit = celestialBody.getKeplerianOrbit();
        if (keplerianOrbit == null) {
            ReferenceFrame referenceFrame = model.getRootReferenceFrame();
            Instant instant = instantManager.newZeroInstance(model, celestialBody, referenceFrame, newTimestamp);
            return instant;
        }

        KeplerianElements keplerianElements = keplerianUtils.fromTimestamp(keplerianOrbit, newTimestamp);
        CartesianState cartesianState = coordinateModels.transform(keplerianElements);

        Instant newInstant = instantManager.newInstant(model, celestialBody, cartesianState, keplerianElements, newTimestamp);
        rotationPropagator.compute(celestialBody, newInstant, newTimestamp);
        return newInstant;
    }
}
