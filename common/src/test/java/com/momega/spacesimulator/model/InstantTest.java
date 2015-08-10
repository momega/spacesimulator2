package com.momega.spacesimulator.model;

import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.utils.TimeUtils;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Created by martin on 8/10/15.
 */
public class InstantTest {

    @Test
    public void addAndGet() {
        InstantManager instantManager = new InstantManager();
        Model model = new Model();
        MovingObject movingObject = new CelestialBody();
        movingObject.setName("X");
        CartesianState cartesianState = new CartesianState();

        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2015, 9, 23, 12, 0, DateTimeZone.UTC));

        KeplerianElements keplerianElements = new KeplerianElements();

        Instant newInstant = instantManager.newInstant(model, movingObject, cartesianState, keplerianElements, timestamp);

        Instant getInstant = instantManager.getInstant(model, movingObject, timestamp);
        Assert.assertSame(newInstant, getInstant);
    }
}
