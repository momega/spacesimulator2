package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.common.CoordinateModels;
import com.momega.spacesimulator.common.KeplerianUtils;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
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

    public void insertCentralBody(Model model, CelestialBody celestialBody) {
        model.getMovingObjects().add(celestialBody);
    }

    public void updateMovingObject(PhysicalBody physicalBody, double mass) {
        physicalBody.setMass(mass * 1E24);
    }

    public void updateMovingObject(RotatingObject rotatingObject, double mass, double radius) {
        updateMovingObject(rotatingObject, mass);
        rotatingObject.setRadius(radius * 1E6);
    }

    public void insertSpacecraft(Model model, Spacecraft spacecraft) {
        model.getMovingObjects().add(spacecraft);
    }

    public void insertCelestialBody(Model model, CelestialBody celestialBody, Timestamp timestamp) {
        model.getMovingObjects().add(celestialBody);

        keplerianPropagator.compute(model, celestialBody, timestamp);
    }

}
