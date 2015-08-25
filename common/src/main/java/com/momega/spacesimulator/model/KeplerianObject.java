package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.MathUtils;

/**
 * Created by martin on 8/25/15.
 */
public abstract class KeplerianObject extends MovingObject {

    private KeplerianOrbit keplerianOrbit;
    private double mi = 0;

    public void setKeplerianOrbit(KeplerianOrbit keplerianOrbit) {
        this.keplerianOrbit = keplerianOrbit;
    }

    public KeplerianOrbit getKeplerianOrbit() {
        return keplerianOrbit;
    }

    public abstract double getMass();

    public double getGravitationParameter() {
        if (mi == 0) {
            mi = getMass() * MathUtils.G;
        }
        return mi;
    }

}
