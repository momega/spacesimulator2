package com.momega.spacesimulator.model;

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
     * Gets the position of the frame relative to parent frame
     * @return
     */
    CartesianState getCartesianState();

    /**
     * Gets the gravitation parameter of the central body of the reference frame
     * @return
     */
    double getGravitationParameter();

}
