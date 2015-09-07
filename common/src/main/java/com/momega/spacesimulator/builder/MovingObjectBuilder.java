package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.utils.RotationUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
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

    @Autowired
    private RotationUtils rotationUtils;

    public KeplerianOrbit createKeplerianOrbit(KeplerianObject keplerianObject, ReferenceFrameDefinition referenceFrameDefinition, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double timeOfPeriapsis, double inclination, double ascendingNode) {
        Assert.notNull(keplerianObject);
        Assert.notNull(referenceFrameDefinition);

        KeplerianOrbit orbit = new KeplerianOrbit();
        orbit.setReferenceFrameDefinition(referenceFrameDefinition);
        orbit.setSemimajorAxis(semimajorAxis);
        orbit.setEccentricity(eccentricity);
        orbit.setArgumentOfPeriapsis(Math.toRadians(argumentOfPeriapsis));
        orbit.setInclination(Math.toRadians(inclination));
        orbit.setAscendingNode(Math.toRadians(ascendingNode));
        orbit.setPeriod(period * DateTimeConstants.SECONDS_PER_DAY);
        orbit.setTimeOfPeriapsis(TimeUtils.fromJulianDay(timeOfPeriapsis));
        orbit.setMeanMotion(2 * Math.PI / orbit.getPeriod());

        keplerianObject.setKeplerianOrbit(orbit);
        return orbit;
    }

    public void updateMovingObject(PhysicalBody physicalBody, double mass) {
        physicalBody.setMass(mass * 1E24);
    }

    public void updateMovingObject(CelestialBody celestialBody, double mass, double radius, double rotationPeriod, double primeMeridian, double ra, double dec) {
        updateMovingObject(celestialBody, mass);
        celestialBody.setPrimeMeridian(Math.toRadians(primeMeridian));
        celestialBody.setRotationPeriod(rotationPeriod * DateTimeConstants.SECONDS_PER_DAY);
        celestialBody.setRadius(radius * 1E6);
        celestialBody.setRa(Math.toRadians(ra));
        celestialBody.setDec(Math.toRadians(dec));
        Rotation axialTilt = rotationUtils.getAxialTilt(celestialBody.getRa(), celestialBody.getDec(), celestialBody.getPrimeMeridian(), true);
        celestialBody.setAxialTilt(axialTilt);
    }

    public void addToBaryCentre(BaryCentre baryCentre, PhysicalBody physicalBody) {
        baryCentre.getPhysicalBodies().add(physicalBody);
    }

    public void insertSpacecraft(Model model, Spacecraft spacecraft) {
        model.getMovingObjects().add(spacecraft);
    }

    public void insertCelestialBody(Model model, CelestialBody celestialBody, Timestamp timestamp) {
        model.getMovingObjects().add(celestialBody);

        keplerianPropagator.compute(model, celestialBody, timestamp);
    }

}
