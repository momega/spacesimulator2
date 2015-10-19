package com.momega.spacesimulator.model;

/**
 * Created by martin on 9/28/15.
 */
public class Apsis extends MovingObject {

    private ApsisType type;

    public ApsisType getType() {
        return type;
    }

    public void setType(ApsisType type) {
        this.type = type;
    }
}
