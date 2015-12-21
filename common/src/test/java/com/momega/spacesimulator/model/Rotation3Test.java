package com.momega.spacesimulator.model;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Assert;
import org.junit.Test;

import com.momega.spacesimulator.service.utils.MathUtils;
import com.momega.spacesimulator.service.utils.RotationUtils;

/**
 * Created by martin on 10/17/14.
 */
public class Rotation3Test {

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
    
    @Test
    public void backwardTest() {
    	Vector3D v = new Vector3D(774.1760814968, 1455.17306494, -315.4483668424);
    	Vector3D vvv = v.negate();
    	Rotation r = new Rotation(v, vvv);
    	Vector3D vv = r.applyTo(v);
    	
    	Assert.assertTrue(vv.getX()<0);
    	Assert.assertTrue(vv.getY()<0);
    	Assert.assertTrue(vv.getZ()>0);
    }

}
