package com.momega.spacesimulator.dynamic;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.CentralReferenceFrame;
import com.momega.spacesimulator.model.Timestamp;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 8/9/15.
 */
@Component
public class ReferenceFrameManager {

    public CentralReferenceFrame createCentralReferenceFrame(CelestialBody celestialBody, Timestamp timestamp) {
        CentralReferenceFrame result = new CentralReferenceFrame();
        result.setCelestialBody(celestialBody);
        CartesianState cartesianState = new CartesianState();
        cartesianState.setPosition(Vector3D.ZERO);
        cartesianState.setVelocity(Vector3D.ZERO);
        cartesianState.setTimestamp(timestamp);
        result.setCartesianState(cartesianState);
        return result;
    }
}
