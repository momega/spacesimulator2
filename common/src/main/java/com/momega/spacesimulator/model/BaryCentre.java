package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 8/25/15.
 */
public class BaryCentre extends KeplerianObject {

    private List<PhysicalBody> physicalBodies = new ArrayList<>();
    private double combinesMass = 0;

    public List<PhysicalBody> getPhysicalBodies() {
        return physicalBodies;
    }

    public void setPhysicalBodies(List<PhysicalBody> physicalBodies) {
        this.physicalBodies = physicalBodies;
    }

    public double getMass() {
        if (combinesMass == 0) {
            for(PhysicalBody pb : getPhysicalBodies()) {
                combinesMass += pb.getMass();
            }
        }
        return combinesMass;
    }

}
