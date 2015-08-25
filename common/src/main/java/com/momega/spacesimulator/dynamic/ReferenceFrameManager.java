package com.momega.spacesimulator.dynamic;

import com.momega.spacesimulator.model.BasicReferenceFrame;
import com.momega.spacesimulator.model.KeplerianObject;
import com.momega.spacesimulator.model.ReferenceFrame;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 8/9/15.
 */
@Component
public class ReferenceFrameManager {

    public ReferenceFrame create(KeplerianObject keplerianObject, ReferenceFrame parent) {
        BasicReferenceFrame referenceFrame = new BasicReferenceFrame();
        referenceFrame.setParent(parent);
        referenceFrame.setKeplerianObject(keplerianObject);
        return referenceFrame;
    }
}
