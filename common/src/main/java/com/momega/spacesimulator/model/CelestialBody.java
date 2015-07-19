package com.momega.spacesimulator.model;

/**
 * Created by martin on 7/19/15.
 */
public class CelestialBody extends PhysicalBody {

    private ReferenceFrame parent;

    @Override
    public ReferenceFrame getParent() {
        return parent;
    }

    public void setParent(ReferenceFrame parent) {
        this.parent = parent;
    }
}
