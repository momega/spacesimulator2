package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.RotationUtils;
import junit.framework.Assert;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by martin on 10/17/14.
 */
public class Rotation3Test {

    private static final Logger logger = LoggerFactory.getLogger(Rotation3Test.class);

    private RotationUtils rotationUtils = new RotationUtils();

    @Test
    public void rotationTest() {
        double alpha = Math.toRadians(317.68143);
        double delta = Math.toRadians(52.8865);

        Rotation r = rotationUtils.getAxialTilt(alpha, delta, 0d, true);
        Vector3D northPole = r.applyTo(Vector3D.PLUS_K);
        Vector3D meridian = r.applyTo(Vector3D.PLUS_I);

        Rotation r2 = new Rotation(Vector3D.PLUS_K, Math.toRadians(45));
        r = r.applyTo(r2);

        Vector3D northPole2 = r.applyTo(Vector3D.PLUS_K);
        Vector3D meridian2 = r.applyTo(Vector3D.PLUS_I);

        Assert.assertTrue(MathUtils.equals(northPole2, northPole, 0.000001));
        double angle = Math.toDegrees(Vector3D.angle(meridian, meridian2));

        Assert.assertEquals(45, angle, 0.0001);
    }
}
