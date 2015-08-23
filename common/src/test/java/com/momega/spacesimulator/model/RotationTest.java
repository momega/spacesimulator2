package com.momega.spacesimulator.model;

import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.RotationUtils;
import junit.framework.Assert;
import org.apache.commons.math3.geometry.euclidean.threed.SphericalCoordinates;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by martin on 7/18/14.
 */
public class RotationTest {

    private static final Logger logger = LoggerFactory.getLogger(RotationTest.class);

    private RotationUtils rotationUtils = new RotationUtils();

    @Test
    public void marsTest() {
        double alpha = Math.toRadians(317.68143);
        double delta = Math.toRadians(52.88650);

        Vector3D v = new SphericalCoordinates(1, alpha, Math.PI/2 - delta).getCartesian();
        SphericalCoordinates angles2 = new SphericalCoordinates(v);
        logger.info("directly dec = {}, ra = {}", 90-Math.toDegrees(angles2.getTheta()), Math.toDegrees(angles2.getPhi()));

        Vector3D northPole = rotationUtils.getNorthPoleVector(alpha, delta, 0, false);

        SphericalCoordinates angles = new SphericalCoordinates(northPole);
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles.getTheta()), Math.toDegrees(angles.getPhi()));

        Assert.assertTrue(MathUtils.equals(v, northPole, 0.00000001));
    }

    @Test
    public void earthTest() {
        double alpha = Math.toRadians(0);
        double delta = Math.toRadians(90);

        Vector3D v = new SphericalCoordinates(1, alpha, Math.PI/2 - delta).getCartesian();
        SphericalCoordinates angles2 = new SphericalCoordinates(v);
        logger.info("directly dec = {}, ra = {}", 90-Math.toDegrees(angles2.getTheta()), Math.toDegrees(angles2.getPhi()));

        Vector3D northPole = rotationUtils.getNorthPoleVector(alpha, delta, 0, false);

        SphericalCoordinates angles = new SphericalCoordinates(northPole);
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles.getTheta()), Math.toDegrees(angles.getPhi()));

        Assert.assertTrue(MathUtils.equals(v, northPole, 0.00000001));
    }

    @Test
    public void earthEclipticTest() {
        double alpha = Math.toRadians(90.00000);
        double delta = Math.toRadians(66.56071);

        Vector3D v = new SphericalCoordinates(1, alpha, Math.PI/2 - delta).getCartesian();
        SphericalCoordinates angles2 = new SphericalCoordinates(v);
        logger.info("directly dec = {}, ra = {}", 90-Math.toDegrees(angles2.getTheta()), Math.toDegrees(angles2.getPhi()));

        alpha = Math.toRadians(0);
        delta = Math.toRadians(90);
        Vector3D northPole = rotationUtils.getNorthPoleVector(alpha, delta, 190.0, true);

        SphericalCoordinates angles = new SphericalCoordinates(northPole);
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles.getTheta()), Math.toDegrees(angles.getPhi()));

        Assert.assertTrue(MathUtils.equals(v, northPole, 0.000001));
    }

    @Test
    public void marsEclipticTest() {
        double alpha = Math.toRadians(352.90764);
        double delta = Math.toRadians(63.28205);

        Vector3D v = new SphericalCoordinates(1, alpha, Math.PI/2 - delta).getCartesian();
        SphericalCoordinates angles2 = new SphericalCoordinates(v);
        logger.info("directly dec = {}, ra = {}", 90-Math.toDegrees(angles2.getTheta()), Math.toDegrees(angles2.getPhi()));

        alpha = Math.toRadians(317.68143);
        delta = Math.toRadians(52.8865);
        Vector3D northPole = rotationUtils.getNorthPoleVector(alpha, delta, 0, true);

        SphericalCoordinates angles = new SphericalCoordinates(northPole);
        logger.info("trans dec = {}, ra = {}", 90-Math.toDegrees(angles.getTheta()), Math.toDegrees(angles.getPhi()));

        Assert.assertTrue(MathUtils.equals(v, northPole, 0.000001));
    }

}
