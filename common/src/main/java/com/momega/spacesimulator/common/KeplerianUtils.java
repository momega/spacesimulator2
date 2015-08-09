package com.momega.spacesimulator.common;

import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.MathUtils;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by martin on 7/19/15.
 */
@Component
public class KeplerianUtils {

    private final static double MINOR_ERROR = Math.pow(10, -12);

    /**
     * Computes the keplerian elements of the given keplerian orbit and the given timestamp. The method
     * is used for Keplerian propagator to compute current position and velocity of the celestial bodies
     * such as planets.
     * @param keplerianOrbit the keplerian orbit
     * @param timestamp
     * @return returns new instance of keplerian elements
     */
    public KeplerianElements fromTimestamp(KeplerianOrbit keplerianOrbit, Timestamp timestamp) {
        double dt = timestamp.subtract(keplerianOrbit.getTimeOfPeriapsis());
        double meanAnomaly = keplerianOrbit.getMeanMotion() * dt;   // mean anomaly

        KeplerianElements keplerianElements = new KeplerianElements();
        keplerianElements.setKeplerianOrbit(keplerianOrbit);

        if (keplerianOrbit.isHyperbolic()) {
            double HA = solveHyperbolicAnomaly(keplerianOrbit, meanAnomaly);
            keplerianElements.setHyperbolicAnomaly(HA);
            keplerianElements.setEccentricAnomaly(null);
        } else {
            meanAnomaly = MathUtils.normalizeAngle(meanAnomaly);
            double EA = solveEccentricAnomaly(keplerianOrbit, meanAnomaly);
            keplerianElements.setEccentricAnomaly(EA);
            keplerianElements.setHyperbolicAnomaly(null);
        }
        solveTheta(keplerianElements);
        keplerianElements.setTimestamp(timestamp);
        return keplerianElements;
    }

    public void solveTheta(KeplerianElements keplerianElements) {
        double EHA;
        KeplerianOrbit keplerianOrbit = keplerianElements.getKeplerianOrbit();
        if (keplerianOrbit.isHyperbolic()) {
            EHA = keplerianElements.getHyperbolicAnomaly();
        } else {
            EHA = keplerianElements.getEccentricAnomaly();
        }
        double theta = solveTheta(EHA, keplerianOrbit.getEccentricity());
        keplerianElements.setTrueAnomaly(theta);
    }

    /**
     * Solve true anomaly from eccentric anomaly for elliptic orbit or from hyperbolic anomaly for hyperbolic orbit
     * @param EHA angle in radians
     * @param eccentricity the eccentricity
     * @return the true anonaly
     */
    public double solveTheta(double EHA, double eccentricity) {
        if (eccentricity >1) {
            return solveThetaFromHA(EHA, eccentricity);
        }
        return solveThetaFromEA(EHA, eccentricity);
    }

    private double solveThetaFromHA(double HA, double eccentricity) {
        double param = FastMath.sqrt((eccentricity + 1) / (eccentricity -1));
        double theta = 2 * FastMath.atan(param * FastMath.tanh(HA / 2));
        if (theta < 0) {
            theta = Math.PI * 2 + theta;
        }
        return theta;
    }

    private double solveThetaFromEA(double EA, double eccentricity) {
        double cosTheta = (FastMath.cos(EA) - eccentricity) / (1.0 - eccentricity * FastMath.cos(EA));
        double theta;
        if (EA < 0) {
            theta = 2 * Math.PI - FastMath.acos(cosTheta);
        } else if (EA < Math.PI) {
            theta = FastMath.acos(cosTheta);
        } else {
            theta = 2 * Math.PI - FastMath.acos(cosTheta);
        }
        return theta;
    }

    /**
     * Computes the eccentric anomaly from mean anomaly. It is the solution of the Kepler equations.
     * <code>
     *     M = E - e * sin(E)
     * </code>
     * @param meanAnomaly the mean anomaly
     * @return the eccentric anomaly
     */
    private double solveEccentricAnomaly(KeplerianOrbit keplerianOrbit, double meanAnomaly) {
        double eccentricity = keplerianOrbit.getEccentricity();
        double E = Math.PI;

        double ratio = 1;
        while (FastMath.abs(ratio) > MINOR_ERROR) {
            ratio = (E - eccentricity * FastMath.sin(E) - meanAnomaly) / (1 - eccentricity * FastMath.cos(E));
            E = E - ratio;
        }

        return E;
    }

    /**
     * Computes the hyperbolic anomaly from mean anomaly. It is the solution of the Kepler equations of the
     * hyperbolic trajctory.
     * <code>
     * 		M = e * sinh(H) - H
     * </code>
     * @param keplerianOrbit the keplerian orbit
     * @param M mean anomaly
     * @return the value of the hyperbolic anomaly
     */
    private double solveHyperbolicAnomaly(KeplerianOrbit keplerianOrbit, double M) {
        double eccentricity = keplerianOrbit.getEccentricity();
        double H = M;
        double ratio = 1;
        while (Math.abs(ratio) > MINOR_ERROR) {
            ratio = (eccentricity * FastMath.sinh(H) - H - M) / (eccentricity * FastMath.cosh(H) - 1);
            H = H - ratio;
        }
        return H;
    }


    /**
     * Gets the position in Cartesian state based on the keplerian elements with given angle theta. So it means the position
     * is defined by the keplerian elements except the angle theta.
     * @return the 3d vector
     */
    public Vector3D getCartesianPosition(KeplerianElements keplerianElements) {
        KeplerianOrbit keplerianOrbit = keplerianElements.getKeplerianOrbit();
        return getCartesianPosition(keplerianOrbit, keplerianElements.getTrueAnomaly());
    }

    public Vector3D getCartesianPosition(KeplerianOrbit keplerianOrbit, double trueAnomaly) {
        double argumentOfPeriapsis = keplerianOrbit.getArgumentOfPeriapsis();
        double e = keplerianOrbit.getEccentricity();
        double r = keplerianOrbit.getSemimajorAxis() * (1 - e * e) / (1 + e * FastMath.cos(trueAnomaly));
        double inclination = keplerianOrbit.getInclination();
        double ascendingNode = keplerianOrbit.getAscendingNode();
        Vector3D p = getCartesianPosition(r, trueAnomaly, inclination, ascendingNode, argumentOfPeriapsis);
        return keplerianOrbit.getReferenceFrame().getCartesianState().getPosition().add(p);
    }

    private static Vector3D getCartesianPosition(double r, double theta, double inclination, double ascendingNode, double argumentOfPeriapsis) {
        double u = theta + argumentOfPeriapsis;
        double x = r * (FastMath.cos(u) * FastMath.cos(ascendingNode) - FastMath.sin(u) * FastMath.cos(inclination) * FastMath.sin(ascendingNode));
        double y = r * (FastMath.cos(u) * FastMath.sin(ascendingNode) + FastMath.sin(u) * FastMath.cos(inclination) * FastMath.cos(ascendingNode));
        double z = r * (FastMath.sin(u) * FastMath.sin(inclination));
        return new Vector3D(x, y, z);
    }

    public Vector3D getCartesianVelocity(KeplerianElements keplerianElements) {
        KeplerianOrbit keplerianOrbit = keplerianElements.getKeplerianOrbit();
        return getCartesianVelocity(keplerianOrbit, keplerianElements.getTrueAnomaly());
    }

    public Vector3D getCartesianVelocity(KeplerianOrbit keplerianOrbit, double trueAnomaly) {
        double e = keplerianOrbit.getEccentricity();
        double mi = keplerianOrbit.getReferenceFrame().getGravitationParameter();
        Vector3D v = getCartesianVelocity(keplerianOrbit.getSemimajorAxis(), mi, trueAnomaly, e,
                keplerianOrbit.getInclination(), keplerianOrbit.getAscendingNode(), keplerianOrbit.getArgumentOfPeriapsis());
        return keplerianOrbit.getReferenceFrame().getCartesianState().getVelocity().add(v);
    }

    public static Vector3D getCartesianVelocity(double a, double mi, double theta, double e, double inclination, double OMEGA, double omega) {
        double param = FastMath.cos(theta) + e;
        double p =a * (1-e*e);
        double sqrtMdivP = FastMath.sqrt(mi/p);

        double x = sqrtMdivP * (param * (-FastMath.sin(omega)*FastMath.cos(OMEGA)-FastMath.cos(inclination)*FastMath.sin(OMEGA)*FastMath.cos(omega))
                - FastMath.sin(theta)*(FastMath.cos(omega)*FastMath.cos(OMEGA)-FastMath.cos(inclination)*FastMath.sin(OMEGA)*FastMath.sin(omega)));

        double y = sqrtMdivP * (param * (-FastMath.sin(omega)*FastMath.sin(OMEGA)+FastMath.cos(inclination)*FastMath.cos(OMEGA)*FastMath.cos(omega))
                - FastMath.sin(theta)*(FastMath.cos(omega)*FastMath.sin(OMEGA)+FastMath.cos(inclination)*FastMath.cos(OMEGA)*FastMath.sin(omega)));

        double z = sqrtMdivP * (param * FastMath.sin(inclination) * FastMath.cos(omega) - FastMath.sin(theta)*FastMath.sin(inclination)*FastMath.sin(omega));
        return new Vector3D(x, y, z);
    }

    public Timestamp timeToAngle(KeplerianElements keplerianElements, Timestamp timestamp, double targetTheta, boolean future) {
        Assert.isTrue(!Double.isNaN(targetTheta), "true anomaly is invalid");
        Assert.notNull(keplerianElements);
        KeplerianOrbit keplerianOrbit = keplerianElements.getKeplerianOrbit();
        double e = keplerianOrbit.getEccentricity();
        double targetM, initM;
        if (!keplerianOrbit.isHyperbolic()) {
            double targetE = solveEA(e, targetTheta);
            targetM = targetE - e * FastMath.sin(targetE);
            initM = keplerianElements.getEccentricAnomaly() - e * FastMath.sin(keplerianElements.getEccentricAnomaly());
        } else {
            double targetE = solveHA(e, targetTheta);
            targetM = e * FastMath.sinh(targetE) - targetE;
            initM = e* FastMath.sinh(keplerianElements.getHyperbolicAnomaly()) - keplerianElements.getHyperbolicAnomaly();
        }

        double diffM = (targetM - initM);
        double n = keplerianOrbit.getMeanMotion();
        if (Double.isNaN(n) || Double.isInfinite(n)) {
            throw new IllegalStateException("undefined mean motion");
        }
        if (n == 0) {
            throw new IllegalStateException("mean motion is zero");
        }
        if (diffM < 0 && future) {
            diffM = targetM + 2 * Math.PI - initM;
        }

        double timeInterval = diffM / n;
        return timestamp.add(timeInterval);
    }

    public double solveEA(double eccentricity, double theta) {
        double param = FastMath.sqrt((1+eccentricity)/(1-eccentricity));
        return 2 * FastMath.atan(FastMath.tan(theta/2) / param);
    }

    private double solveHA(double eccentricity, double theta) {
        double sinH = (FastMath.sin(theta) * FastMath.sqrt(eccentricity*eccentricity -1)) / (1 + eccentricity * Math.cos(theta));
        double HA = FastMath.asinh(sinH);
        //double cosHA = (eccentricity + Math.cos(theta))/(1 + eccentricity*Math.cos(theta));
        //double HA = MathUtils.acosh(cosHA);
        return HA;
    }

}
