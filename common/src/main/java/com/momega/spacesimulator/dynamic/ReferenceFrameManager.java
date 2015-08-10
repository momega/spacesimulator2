package com.momega.spacesimulator.dynamic;

import com.momega.spacesimulator.model.*;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 8/9/15.
 */
@Component
public class ReferenceFrameManager {

    public ReferenceFrame createByCelestialBody(CelestialBody celestialBody) {
        CelestialBodyReferenceFrame referenceFrame = new CelestialBodyReferenceFrame();
        referenceFrame.setCelestialBody(celestialBody);
        return referenceFrame;
    }
}
