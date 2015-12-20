package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.ReferenceFrameDefinition;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.TimeInterval;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.ManeuverService;
import com.momega.spacesimulator.service.utils.TimeUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by martin on 11/2/15.
 */
public class ManeuverServiceTest {

    private ManeuverService maneuverService = new ManeuverService();
    
    ReferenceFrameDefinition rfd = new ReferenceFrameDefinition();

    @Test
    public void addTest() {
        Spacecraft s = new Spacecraft();
        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 16, 0, DateTimeZone.UTC));
        TimeInterval i1 = TimeUtils.createInterval(timestamp, 120);
        Maneuver m1 =  new Maneuver();
        m1.setReferenceFrameDefinition(rfd);
        m1.setInterval(i1);
        m1.setThrottle(1.0);

        maneuverService.addManeuver(m1, s);

        Timestamp t2 = timestamp.add(60);

        Maneuver m = maneuverService.findManeuver(s, t2);
        Assert.assertNotNull(m);
        Assert.assertSame(m1, m);
    }

    @Test
    public void negativeTest() {
        Spacecraft s = new Spacecraft();
        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 16, 0, DateTimeZone.UTC));
        TimeInterval i1 = TimeUtils.createInterval(timestamp, 120);
        Maneuver m1 =  new Maneuver();
        m1.setReferenceFrameDefinition(rfd);
        m1.setInterval(i1);
        m1.setThrottle(1.0);

        maneuverService.addManeuver(m1, s);

        TimeInterval i2 = TimeUtils.createInterval(timestamp.add(240), 60);
        Maneuver m2 =  new Maneuver();
        m2.setReferenceFrameDefinition(rfd);
        m2.setInterval(i2);
        m2.setThrottle(1.0);

        maneuverService.addManeuver(m2, s);

        Maneuver m = maneuverService.findManeuver(s, timestamp.add(180));
        Assert.assertNull(m);
    }

    @Test
    public void doubleTest() {
        Spacecraft s = new Spacecraft();
        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 16, 0, DateTimeZone.UTC));
        TimeInterval i1 = TimeUtils.createInterval(timestamp, 120);
        Maneuver m1 =  new Maneuver();
        m1.setReferenceFrameDefinition(rfd);
        m1.setInterval(i1);
        m1.setThrottle(1.0);

        maneuverService.addManeuver(m1, s);

        TimeInterval i2 = TimeUtils.createInterval(timestamp.add(240), 60);
        Maneuver m2 =  new Maneuver();
        m2.setReferenceFrameDefinition(rfd);
        m2.setInterval(i2);
        m2.setThrottle(1.0);

        maneuverService.addManeuver(m2, s);

        Maneuver m = maneuverService.findManeuver(s, timestamp.add(270));
        Assert.assertNotNull(m);
        Assert.assertSame(m2, m);
    }
}
