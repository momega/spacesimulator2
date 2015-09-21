package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.common.CoordinateModels;
import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.utils.RotationUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

    @Autowired
    private CoordinateModels coordinateModels;

    @Autowired
    private InstantManager instantManager;

    public KeplerianOrbit createKeplerianOrbit(ReferenceFrameDefinition referenceFrameDefinition, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, Timestamp timeOfPeriapsis, double inclination, double ascendingNode) {
        KeplerianOrbit orbit = new KeplerianOrbit();
        orbit.setReferenceFrameDefinition(referenceFrameDefinition);
        orbit.setSemimajorAxis(semimajorAxis);
        orbit.setEccentricity(eccentricity);
        orbit.setArgumentOfPeriapsis(Math.toRadians(argumentOfPeriapsis));
        orbit.setInclination(Math.toRadians(inclination));
        orbit.setAscendingNode(Math.toRadians(ascendingNode));
        orbit.setPeriod(period);
        orbit.setTimeOfPeriapsis(timeOfPeriapsis);
        orbit.setMeanMotion(2 * Math.PI / orbit.getPeriod());
        return orbit;
    }

    public KeplerianOrbit createAndSetKeplerianOrbit(KeplerianObject keplerianObject, ReferenceFrameDefinition referenceFrameDefinition, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double timeOfPeriapsis, double inclination, double ascendingNode) {
        Assert.notNull(keplerianObject);
        Assert.notNull(referenceFrameDefinition);

        Timestamp t = TimeUtils.fromJulianDay(timeOfPeriapsis);
        KeplerianOrbit orbit = createKeplerianOrbit(referenceFrameDefinition, semimajorAxis, eccentricity, argumentOfPeriapsis, period * DateTimeConstants.SECONDS_PER_DAY, t, inclination, ascendingNode);
        keplerianObject.setKeplerianOrbit(orbit);
        return orbit;
    }

    public Instant insertSpacecraft(Model model, Spacecraft spacecraft, KeplerianOrbit keplerianOrbit, Timestamp timestamp) {
        model.getMovingObjects().add(spacecraft);

        Instant instant = keplerianPropagator.computeFromOrbit(model, spacecraft, keplerianOrbit, timestamp);
        return instant;

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

    public void insertKeplerianObject(Model model, KeplerianObject keplerianObject, Timestamp timestamp) {
        model.getMovingObjects().add(keplerianObject);

        keplerianPropagator.compute(model, keplerianObject, timestamp);
    }

}
