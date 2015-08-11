package com.momega.spacesimulator.dynamic;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.CelestialBodyReferenceFrame;
import com.momega.spacesimulator.model.ReferenceFrame;
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
