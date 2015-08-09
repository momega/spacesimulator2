package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.MathUtils;

/**
 * Physical body in the space. It is the {@link MovingObject} with the mass and the given orientation.
 * Created by martin on 7/19/15.
 */
public abstract class PhysicalBody extends MovingObject {

    private double mass;
    private double mi = 0;

    public double getGravitationParameter() {
        if (mi == 0) {
            mi = this.mass * MathUtils.G;
        }
        return mi;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
}
