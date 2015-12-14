package com.momega.spacesimulator.service.propagator.feature;

import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;
import org.springframework.core.Ordered;

/**
 * Created by martin on 9/28/15.
 */
public interface PropagatorFeature extends Ordered {

    void calculation(Model model, Timestamp timestamp);
}
