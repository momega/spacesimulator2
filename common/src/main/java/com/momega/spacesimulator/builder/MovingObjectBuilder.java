package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.utils.RotationUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by martin on 7/19/15.
 */
@Component
public class MovingObjectBuilder {

    @Autowired
    private KeplerianPropagator keplerianPropagator;

    public KeplerianOrbit createKeplerianOrbit(CelestialBody celestialBody, ReferenceFrame referenceFrame, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double timeOfPeriapsis, double inclination, double ascendingNode) {
        Assert.notNull(celestialBody);
        Assert.notNull(referenceFrame);

        KeplerianOrbit orbit = new KeplerianOrbit();
        orbit.setReferenceFrame(referenceFrame);
        orbit.setSemimajorAxis(semimajorAxis);
        orbit.setEccentricity(eccentricity);
        orbit.setArgumentOfPeriapsis(Math.toRadians(argumentOfPeriapsis));
        orbit.setInclination(Math.toRadians(inclination));
        orbit.setAscendingNode(Math.toRadians(ascendingNode));
        orbit.setPeriod(period * DateTimeConstants.SECONDS_PER_DAY);
        orbit.setTimeOfPeriapsis(TimeUtils.fromJulianDay(timeOfPeriapsis));
        orbit.setMeanMotion(2 * Math.PI / orbit.getPeriod());

        celestialBody.setKeplerianOrbit(orbit);
        return orbit;
    }

    public void updateMovingObject(PhysicalBody physicalBody, double mass) {
        physicalBody.setMass(mass * 1E24);
    }

    public void updateMovingObject(RotatingObject rotatingObject, double mass, double radius, double rotationPeriod, double primeMeridian, double ra, double dec) {
        updateMovingObject(rotatingObject, mass);
        rotatingObject.setPrimeMeridian(Math.toRadians(primeMeridian));
        rotatingObject.setRotationPeriod(rotationPeriod * DateTimeConstants.SECONDS_PER_DAY);
        rotatingObject.setRadius(radius * 1E6);
        rotatingObject.setRa(Math.toRadians(ra));
        rotatingObject.setDec(Math.toRadians(dec));
    }

    public void insertSpacecraft(Model model, Spacecraft spacecraft) {
        model.getMovingObjects().add(spacecraft);
    }

    public void insertCelestialBody(Model model, CelestialBody celestialBody, Timestamp timestamp) {
        model.getMovingObjects().add(celestialBody);

        keplerianPropagator.compute(model, celestialBody, timestamp);
    }

    /**
     * Creates the rotation transformation
     * @param alpha right ascension
     * @param delta declination of the north-pole
     * @return the transformation matrix
     */
    public static Orientation createOrientation(double alpha, double delta) {
        return RotationUtils.createOrientation(alpha, delta, true);
    }

}
