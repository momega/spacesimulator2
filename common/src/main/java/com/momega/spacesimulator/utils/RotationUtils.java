package com.momega.spacesimulator.utils;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.springframework.stereotype.Component;

import java.util.Vector;

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
     * @param primeMeridian prime meridian
     * @param toEcliptic true if the values relates to the ecliptic. The value false if just for testing purposes
     * @return the rotation
     */
    public Rotation getAxialTilt(double alpha, double delta, double primeMeridian, boolean toEcliptic) {
        double xAngle = (toEcliptic) ? -ECLIPTIC : 0d;
        Rotation tilt = new Rotation(RotationOrder.XZY, xAngle, alpha, Math.PI / 2 - delta);
        Rotation meridian = new Rotation(Vector3D.PLUS_K, primeMeridian);
        Rotation r = tilt.applyTo(meridian);
        return r;
    }

    /**
     * Create vector defines the position of the north pole
     * @param alpha the RA of the axial tilt
     * @param delta the DEC to the axial tilt
     * @param primeMeridian prime meridian
     * @param toEcliptic true if the values relates to the ecliptic. The value false if just for testing purposes
     * @return the rotation
     */
    public Vector3D getNorthPoleVector(double alpha, double delta, double primeMeridian, boolean toEcliptic) {
        Rotation r = getAxialTilt(alpha, delta, primeMeridian, toEcliptic);
        return getAxisVector(r, Vector3D.PLUS_K);
    }

    public Vector3D getAxisVector(Rotation r, Vector3D axis) {
        Vector3D result = r.applyTo(axis);
        return result;
    }

}
