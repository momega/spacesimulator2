package com.momega.spacesimulator.model;

/**
 * The class represents the celestial such as planet. It is the {@link RotatingObject}
 * with defined keplerian orbit
 * Created by martin on 7/19/15.
 */
public class CelestialBody extends RotatingObject {

    private KeplerianOrbit keplerianOrbit;

    public void setKeplerianOrbit(KeplerianOrbit keplerianOrbit) {
        this.keplerianOrbit = keplerianOrbit;
    }

    public KeplerianOrbit getKeplerianOrbit() {
        return keplerianOrbit;
    }
}
