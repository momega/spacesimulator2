package com.momega.spacesimulator.utils;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 8/20/15.
 */
@Component
public class RotationUtils {

    public static final double ECLIPTIC = Math.toRadians(23.439291);

    /**
     * Create rotation transformation based on the direction of the axial tilt
     * @param alpha the RA of the axial tilt
     * @param delta the DEC to the axial tilt
     * @param toEcliptic true if the values relates to the ecliptic. The value false if just for testing purposes
     * @return the rotation
     */
    public Rotation getAxialTilt(double alpha, double delta, boolean toEcliptic) {
        double xAngle = (toEcliptic) ? -ECLIPTIC : 0d;
        Rotation r = new Rotation(RotationOrder.XZY, xAngle, alpha, Math.PI / 2 - delta);
        return r;
    }

    /**
     * Create vector defines the position of the north pole
     * @param alpha the RA of the axial tilt
     * @param delta the DEC to the axial tilt
     * @param toEcliptic true if the values relates to the ecliptic. The value false if just for testing purposes
     * @return the rotation
     */
    public Vector3D getNorthPoleVector(double alpha, double delta, boolean toEcliptic) {
        //Orientation orientation = createOrientation(alpha, delta, toEcliptic);
        //return orientation.getV();
        Rotation r = getAxialTilt(alpha, delta, toEcliptic);
        Vector3D result = r.applyTo(Vector3D.PLUS_K);
        return result;
    }

}
