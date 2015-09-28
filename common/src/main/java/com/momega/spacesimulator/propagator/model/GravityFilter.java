package com.momega.spacesimulator.propagator.model;

import com.momega.spacesimulator.model.MovingObject;

/**
 * Created by martin on 9/27/15.
 */
public interface GravityFilter {

    boolean filter(MovingObject movingObject);
}
