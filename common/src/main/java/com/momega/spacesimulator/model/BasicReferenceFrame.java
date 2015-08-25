package com.momega.spacesimulator.model;

/**
 * Created by martin on 8/9/15.
 */
public class BasicReferenceFrame implements ReferenceFrame {

    private KeplerianObject keplerianObject;
    private ReferenceFrame parent;

    @Override
    public ReferenceFrame getParent() {
        return parent;
    }

    public void setParent(ReferenceFrame parent) {
        this.parent = parent;
    }

    @Override
    public CartesianState getCartesianState() {
        return null;
    }

    @Override
    public double getGravitationParameter() {
        return getKeplerianObject().getGravitationParameter();
    }

    public KeplerianObject getKeplerianObject() {
        return keplerianObject;
    }

    public void setKeplerianObject(KeplerianObject keplerianObject) {
        this.keplerianObject = keplerianObject;
    }

    @Override
    public String toString() {
        return "BasicReferenceFrame{" +
                "keplerianObject=" + keplerianObject +
                '}';
    }
}
