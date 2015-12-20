package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.Maneuver;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.utils.TimeUtils;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by martin on 11/2/15.
 */
@Component
public class ManeuverService {

    /**
     * Adds maneuver for the spacecraft
     * @param maneuver the maneuver to add
     * @param spacecraft the spacecraft
     */
    public void addManeuver(Maneuver maneuver, Spacecraft spacecraft) {
    	Assert.notNull(maneuver.getReferenceFrameDefinition());
        spacecraft.getManeuvers().add(maneuver);
    }

    /**
     * Finds the planned maneuver which is valid of the given timestamp
     * @param spacecraft the spacecraft
     * @param timestamp
     * @return
     */
    public Maneuver findManeuver(Spacecraft spacecraft, Timestamp timestamp) {
        for(Maneuver m : spacecraft.getManeuvers()) {
            Assert.notNull(m.getInterval());
            if (TimeUtils.isTimestampInInterval(timestamp, m.getInterval())) {
                return m;
            }
        }
        return null;
    }
}
