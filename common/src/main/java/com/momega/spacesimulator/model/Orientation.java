package com.momega.spacesimulator.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;
import org.springframework.util.Assert;

/**
 * Orientation class is used to define 3D orientation. It is defined with
 * the three mutually-orthogonal axes, namely {@link #n} (points in the
 * direction faced by the object), {@link #u}  (points to the left of the object)
 * and {@link #v}  (points to the top of the object).
 * Created by martin on 9.5.2014.
 */
public class Orientation {

    private Vector3D n;
    private Vector3D u;
    private Vector3D v;

    public Orientation(Vector3D nVector, Vector3D vVector) {
        setN(nVector.normalize());
        setV(vVector.normalize());
        setU(getV().crossProduct(getN()));
    }

    public static Orientation createUnit() {
        return new Orientation(Vector3D.PLUS_I, Vector3D.PLUS_K);
    }

    /**
     * Returns the n vector of the object3d.
     */
    public Vector3D getN() {
        return n;
    }

    /**
     * Returns the u vector of the object3d.
     */
    public Vector3D getU()
    {
        return u;
    }

    /**
     * Returns the v vector of the object3d.
     */
    public Vector3D getV()
    {
        return v;
    }

    public void setN(Vector3D nVector) {
        this.n = nVector;
    }

    public void setU(Vector3D uVector) {
        this.u = uVector;
    }

    public void setV(Vector3D vVector) {
        this.v = vVector;
    }

    /**
     * Rotates the orientation up
     * @param step the angle in radians
     */
    public void lookUp(double step) {
        rotate(this.u, step);
    }

    public void lookLeft(double step) {
        rotate(new Vector3D(0,0,1), step);
    }

    public void lookAroundV(double step) {
        rotate(this.v, step);
    }

    public void twist(double step) {
        rotate(this.n, step);
    }

    /**
     * Rotates the orientation object anticlockwise by the specified angle about the specified axis.
     * @param axis	The axis about which to rotate
     * @param angle	The angle by which to rotate (in radians)
     */
    public void rotate(Vector3D axis, double angle)
    {
        // Note: We try and optimise things a little by observing that there's no point rotating
        // an axis about itself and that generally when we rotate about an axis, we'll be passing
        // it in as the parameter axis, e.g. object3d.rotate(object3d.getN(), Math.PI/2).
        if(axis != getN()) {
            setN(rotateAboutAxis(getN(), angle, axis));
        }
        if(axis != getU()) {
            setU(rotateAboutAxis(getU(), angle, axis));
        }
        if(axis != getV()) {
            setV(rotateAboutAxis(getV(), angle, axis));
        }
    }

    public final static double SMALL_EPSILON = 0.00001;

    /**
     * Rotates vector v anticlockwise about the specified axis by the specified angle (in radians).
     * @param v	The vector to rotate about the axis
     * @param angle	The angle by which to rotate it (in radians)
     * @param axis	The axis about which to rotate it
     * @return	A (new) vector containing the result of the rotation
     */
    public static Vector3D rotateAboutAxis(final Vector3D v, final double angle, final Vector3D axis) {
        Assert.notNull(v);
        Assert.notNull(axis);
        Assert.isTrue(Math.abs(axis.getNorm() - 1) < SMALL_EPSILON);

        // Main algorithm
        double cosAngle = FastMath.cos(angle);
        Vector3D cross = axis.crossProduct(v);

        // ret = v cos radianAngle + (axis x v) sin radianAngle + axis(axis . v)(1 - cos radianAngle)
        // (See Mathematics for 3D Game Programming and Computer Graphics, P.62, for details of why this is (it's not very hard)).
        Vector3D ret = v.scalarMultiply(cosAngle);
        ret = ret.add(Math.sin(angle), cross);
        ret = ret.add(axis.dotProduct(v) * (1 - cosAngle), axis);
        return ret;
    }

}