package com.momega.spacesimulator.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Created by martin on 7/19/15.
 */
public interface ReferenceFrame {

    /**
     * Gets the parent reference frame
     * @return
     */
    ReferenceFrame getParent();

    /**
     * Gets the position of the frame
     * @return
     */
    CartesianState getCartesianState();

    /**
     * Gets the gravitation parameter of the central body of the reference frame
     * @return
     */
    double getGravitationParameter();

}
