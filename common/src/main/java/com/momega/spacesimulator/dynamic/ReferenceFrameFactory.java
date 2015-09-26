package com.momega.spacesimulator.dynamic;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.utils.CartesianUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 9/6/15.
 */
@Component
public class ReferenceFrameFactory {

    @Autowired
    private KeplerianPropagator keplerianPropagator;

    @Autowired
    private CartesianUtils cartesianUtils;

    public ReferenceFrameDefinition createDefinition(KeplerianObject keplerianObject, ReferenceFrameDefinition parent) {
        ReferenceFrameDefinition result = new ReferenceFrameDefinition();
        result.setKeplerianObject(keplerianObject);
        result.setParent(parent);
        return result;
    }

    /**
     * The method returns the instance of the reference frame based on the definition
     * @param referenceFrameDefinition the definition
     * @param model the model
     * @param timestamp the timestamp of the reference frame
     * @return new instance of the reference frame,
     */
    public ReferenceFrame getFrame(ReferenceFrameDefinition referenceFrameDefinition, Model model, Timestamp timestamp) {
        KeplerianObject keplerianObject = referenceFrameDefinition.getKeplerianObject();

        ReferenceFrame referenceFrame = new ReferenceFrame();
        referenceFrame.setGravitationParameter(keplerianObject.getGravitationParameter());
        referenceFrame.setDefinition(referenceFrameDefinition);

        if (referenceFrameDefinition.getParent() == null) {
            referenceFrame.setParent(null);
            CartesianState cartesianState = cartesianUtils.zero();
            referenceFrame.setCartesianState(cartesianState);
            referenceFrame.setTimestamp(timestamp);
        } else {
            Instant instant = keplerianPropagator.get(model, keplerianObject, timestamp);
            CartesianState cartesianState = instant.getCartesianState();
            referenceFrame.setCartesianState(cartesianState);
            referenceFrame.setParent(cartesianState.getReferenceFrame());
            referenceFrame.setTimestamp(timestamp);
        }

        return referenceFrame;
    }
}
