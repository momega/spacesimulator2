package com.momega.spacesimulator.propagator.force;

import com.momega.spacesimulator.model.MovingObject;

/**
 * Created by martin on 9/27/15.
 */
public class EarthGravityFilter implements GravityFilter {

    private final static String EARTH = "Earth";

    @Override
    public boolean filter(MovingObject movingObject) {
        return movingObject.getName().equals(EARTH);
    }
}
