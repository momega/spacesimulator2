package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.common.CoordinateModels;
import com.momega.spacesimulator.common.KeplerianUtils;
import com.momega.spacesimulator.model.*;
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
    private KeplerianUtils keplerianUtils;

    @Autowired
    private CoordinateModels coordinateModels;

    public KeplerianOrbit createKeplerianOrbit(CelestialBody celestialBody, ReferenceFrame referenceFrame, Timestamp timestamp, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double timeOfPeriapsis, double inclination, double ascendingNode) {
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

    public void insertMovingObject(Model model, MovingObject movingObject) {
        model.getMovingObjects().add(movingObject);
    }

}
