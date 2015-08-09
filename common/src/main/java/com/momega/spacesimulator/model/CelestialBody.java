package com.momega.spacesimulator.model;

/**
 * Created by martin on 7/19/15.
 */
public class CelestialBody extends PhysicalBody {

    private KeplerianOrbit keplerianOrbit;

    public void setKeplerianOrbit(KeplerianOrbit keplerianOrbit) {
        this.keplerianOrbit = keplerianOrbit;
    }

    public KeplerianOrbit getKeplerianOrbit() {
        return keplerianOrbit;
    }
}
