package com.momega.spacesimulator.model;

/**
 * Created by martin on 9/6/15.
 */
public class ReferenceFrameDefinition {

    private KeplerianObject keplerianObject;
    private ReferenceFrameDefinition parent;

    public KeplerianObject getKeplerianObject() {
        return keplerianObject;
    }

    public void setKeplerianObject(KeplerianObject keplerianObject) {
        this.keplerianObject = keplerianObject;
    }

    public ReferenceFrameDefinition getParent() {
        return parent;
    }

    public void setParent(ReferenceFrameDefinition parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "ReferenceFrameDefinition{" +
                "keplerianObject=" + keplerianObject.getName() +
                '}';
    }
}
