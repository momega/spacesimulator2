package com.momega.spacesimulator.propagator.feature;

import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;

/**
 * Created by martin on 9/28/15.
 */
public interface PropagatorFeature {

    void calculation(Model model, Timestamp timestamp);
}
