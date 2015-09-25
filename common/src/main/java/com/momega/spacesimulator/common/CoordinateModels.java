package com.momega.spacesimulator.common;

import com.momega.spacesimulator.dynamic.ReferenceFrameFactory;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.KeplerianUtils;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by martin on 7/19/15.
 */
@Component
public class CoordinateModels {

    private final static double MINOR_ERROR = Math.pow(10, -12);

    @Autowired
    private ReferenceFrameFactory referenceFrameFactory;

    @Autowired
    private KeplerianUtils keplerianUtils;

    public KeplerianElements transform(CartesianState cartesianState, Timestamp timestamp) {
        Vector3D position = cartesianState.getPosition();
        Vector3D velocity = cartesianState.getVelocity();
        Vector3D hVector = getAngularMomentum(cartesianState);

        double h = hVector.getNorm();
        double i = FastMath.acos(hVector.getZ() / h);
        double mi = cartesianState.getReferenceFrame().getGravitationParameter();

        Vector3D eVector = new Vector3D(1/mi, velocity.crossProduct(hVector)).subtract(position.normalize());
        double e = eVector.getNorm();

        double a = h*h / (1- e*e) / mi;

        double OMEGA = 0d;
        double omega = 0d; // this is for circular, equatorial orbit
        double theta;

        if (i > MINOR_ERROR) {
            Vector3D nVector = Vector3D.PLUS_K.crossProduct(hVector);
            double n = nVector.getNorm();
            OMEGA = FastMath.acos(nVector.getX() / n);
            if (nVector.getY() < 0) {
                OMEGA = 2 * Math.PI - OMEGA;
            }

            if (e>MINOR_ERROR) {
                omega = FastMath.acos(nVector.dotProduct(eVector) / n / e);
                if (eVector.getZ() < 0) {
                    omega = 2 * Math.PI - omega;
                }

                double thetaCos = eVector.dotProduct(position) / position.getNorm() / e;
                if (thetaCos<-1) {
                    thetaCos = -1;
                } else if (thetaCos > 1) {
                    thetaCos = 1;
                }
                theta = FastMath.acos(thetaCos);

                if (position.dotProduct(velocity) <0) {
                    theta = 2* Math.PI - theta;
                }

            } else {
                theta = FastMath.acos(nVector.dotProduct(position) / n / position.getNorm());
                if (position.getZ()<0) {
                    theta = 2* Math.PI - theta;
                }
            }

        } else {
            if (e>MINOR_ERROR) {
                omega = FastMath.acos(eVector.getX() / e);
                if (eVector.getY() < 0) {
                    omega = 2 * Math.PI - omega;
                }

                theta = FastMath.acos(eVector.dotProduct(position) / e / position.getNorm());
                if (position.dotProduct(velocity) <0) {
                    theta = 2* Math.PI - theta;
                }

            } else {
                theta = FastMath.acos(position.getX() / position.getNorm());
                if (position.getY() <0) {
                    theta = 2* Math.PI - theta;
                }
            }
        }

        KeplerianElements keplerianElements = new KeplerianElements();
        KeplerianOrbit keplerianOrbit = new KeplerianOrbit();
        keplerianElements.setKeplerianOrbit(keplerianOrbit);
        keplerianOrbit.setReferenceFrameDefinition(cartesianState.getReferenceFrame().getDefinition());
        keplerianOrbit.setInclination(i);
        keplerianOrbit.setEccentricity(e);
        keplerianOrbit.setSemimajorAxis(a);
        keplerianOrbit.setAscendingNode(OMEGA);
        keplerianOrbit.setArgumentOfPeriapsis(omega);
        keplerianElements.setTrueAnomaly(theta);

        double meanMotion;
        if (keplerianOrbit.isHyperbolic()) {
            meanMotion = FastMath.sqrt(-mi / (a * a * a));
            double ha = keplerianUtils.solveHA(e, theta);
            keplerianElements.setHyperbolicAnomaly(ha);
            keplerianElements.setEccentricAnomaly(null);
        } else {
            meanMotion = FastMath.sqrt(mi / (a * a * a));
            double ea = keplerianUtils.solveEA(e, theta);
            keplerianElements.setEccentricAnomaly(ea);
            keplerianElements.setHyperbolicAnomaly(null);
        }
        double period = 2* Math.PI / meanMotion;
        keplerianOrbit.setMeanMotion(meanMotion);
        keplerianOrbit.setPeriod(period);

        Timestamp TT = keplerianUtils.timeToAngle(keplerianElements, timestamp, 0.0, false);
        keplerianOrbit.setTimeOfPeriapsis(TT);

        return keplerianElements;
    }

    /**
     * Transfers keplerian elements to cartesian state
     * @param model the model
     * @param timestamp the timestamp
     * @param keplerianElements the instance of the {@link KeplerianElements}
     * @return new instance of cartesian state
     */
    public CartesianState transform(Model model, Timestamp timestamp, KeplerianElements keplerianElements) {
        CartesianState cartesianState = new CartesianState();
        cartesianState.setPosition(keplerianUtils.getCartesianPosition(keplerianElements));
        cartesianState.setVelocity(keplerianUtils.getCartesianVelocity(keplerianElements));

        ReferenceFrameDefinition referenceFrameDefinition = keplerianElements.getKeplerianOrbit().getReferenceFrameDefinition();
        ReferenceFrame referenceFrame = referenceFrameFactory.getFrame(referenceFrameDefinition, model, timestamp);

        cartesianState.setReferenceFrame(referenceFrame);
        return cartesianState;
    }

    public CartesianState add(CartesianState c1, CartesianState c2) {
        CartesianState result = new CartesianState();
        result.setPosition(c1.getPosition().add(c2.getPosition()));
        result.setVelocity(c1.getVelocity().add(c2.getVelocity()));
        result.setReferenceFrame(c1.getReferenceFrame());
        return result;
    }

    public CartesianState transferToParent(CartesianState cartesianState) {
        Assert.notNull(cartesianState);
        CartesianState result = new CartesianState();
        result.setPosition(cartesianState.getPosition().add(cartesianState.getReferenceFrame().getCartesianState().getPosition()));
        result.setVelocity(cartesianState.getVelocity().add(cartesianState.getReferenceFrame().getCartesianState().getVelocity()));
        result.setReferenceFrame(cartesianState.getReferenceFrame().getParent());
        return result;
    }

    public CartesianState transferToRoot(CartesianState cartesianState) {
        ReferenceFrame rf = cartesianState.getReferenceFrame();
        CartesianState result = cartesianState;
        while (rf != null) {
            result = transferToParent(result);
            rf = result.getReferenceFrame();
        }
        return result;
    }

    /**
     * Computes angular momentum
     * @return the angular momentum
     */
    public Vector3D getAngularMomentum(CartesianState cartesianState) {
        return cartesianState.getPosition().crossProduct(cartesianState.getVelocity());
    }

}
