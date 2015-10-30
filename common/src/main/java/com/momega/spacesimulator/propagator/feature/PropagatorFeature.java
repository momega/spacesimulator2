package com.momega.spacesimulator.propagator.feature;

import org.springframework.core.Ordered;

import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;

/**
 * Created by martin on 9/28/15.
 */
public interface PropagatorFeature extends Ordered {

    void calculation(Model model, Timestamp timestamp);
}
