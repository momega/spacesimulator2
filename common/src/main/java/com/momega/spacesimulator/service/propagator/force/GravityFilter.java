package com.momega.spacesimulator.service.propagator.force;

import com.momega.spacesimulator.model.MovingObject;

/**
 * Created by martin on 9/27/15.
 */
public interface GravityFilter {

    boolean filter(MovingObject movingObject);
}
