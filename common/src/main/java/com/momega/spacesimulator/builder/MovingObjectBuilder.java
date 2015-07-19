package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.common.CoordinateModels;
import com.momega.spacesimulator.common.KeplerianUtils;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.TimeUtils;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
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

    public KeplerianElements createKeplerianElements(MovingObject movingObject, ReferenceFrame centralObject, Timestamp timestamp, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double timeOfPeriapsis, double inclination, double ascendingNode) {
        Assert.notNull(movingObject);
        Assert.notNull(centralObject);

        KeplerianOrbit orbit = new KeplerianOrbit();
        orbit.setReferenceFrame(centralObject);
        orbit.setSemimajorAxis(semimajorAxis);
        orbit.setEccentricity(eccentricity);
        orbit.setArgumentOfPeriapsis(Math.toRadians(argumentOfPeriapsis));
        orbit.setInclination(Math.toRadians(inclination));
        orbit.setAscendingNode(Math.toRadians(ascendingNode));
        orbit.setPeriod(period * DateTimeConstants.SECONDS_PER_DAY);
        orbit.setTimeOfPeriapsis(TimeUtils.fromJulianDay(timeOfPeriapsis));
        orbit.setMeanMotion(2* Math.PI / orbit.getPeriod());

        Assert.notNull(movingObject.getName());

        // initialize position

        KeplerianElements keplerianElements = keplerianUtils.fromTimestamp(orbit, timestamp);
        CartesianState cartesianState = coordinateModels.transform(keplerianElements);

        movingObject.setKeplerianElements(keplerianElements);
        movingObject.setCartesianState(cartesianState);

        return keplerianElements;
    }

    public void updateMovingObject(PhysicalBody physicalBody, double mass) {
        physicalBody.setMass(mass * 1E24);
    }

    public void setCentralPoint(MovingObject movingObject, Timestamp timestamp) {
        movingObject.setCartesianState(new CartesianState());
        movingObject.getCartesianState().setPosition(Vector3D.ZERO);
        movingObject.getCartesianState().setVelocity(Vector3D.ZERO);
        movingObject.getCartesianState().setTimestamp(timestamp);
    }
}
