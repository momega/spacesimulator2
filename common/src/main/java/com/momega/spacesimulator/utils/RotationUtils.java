package com.momega.spacesimulator.utils;

import com.momega.spacesimulator.model.Orientation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Created by martin on 8/20/15.
 */
public class RotationUtils {

    public static final double ECLIPTIC = Math.toRadians(23.439291);
    /**
     * Creates the rotation transformation
     * @param alpha right ascension
     * @param delta declination of the north-pole
     * @return the transformation matrix
     */
    public static Orientation createOrientation(double alpha, double delta, boolean toEcliptic) {
        Orientation o = Orientation.createUnit();
        o.lookAroundV(alpha);
        o.lookUp(Math.PI / 2 - delta);
        if (toEcliptic) {
            o.rotate(Vector3D.MINUS_I, ECLIPTIC);
        }
        return o;
    }

}
