package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.KeplerianUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 9/28/15.
 */
@Component
public class ApsisService {

    @Autowired
    private CoordinateService coordinateService;

    @Autowired
    private KeplerianUtils keplerianUtils;

    public Instant getApsis(Model model, ApsisType apsisType, KeplerianOrbit keplerianOrbit, Timestamp timestamp) {
        Apsis apsis = new Apsis();
        apsis.setType(apsisType);

        KeplerianElements keplerianElements = new KeplerianElements();
        keplerianElements.setTrueAnomaly(apsisType.getTrueAnomaly());
        keplerianElements.setKeplerianOrbit(keplerianOrbit);
        keplerianUtils.solveEccentricAnomaly(keplerianElements);

        Timestamp apsisTime = keplerianUtils.timeToAngle(keplerianElements, timestamp, apsisType.getTrueAnomaly(), true);
        CartesianState cartesianState = coordinateService.transform(model, apsisTime, keplerianElements);

        Instant instant = new Instant();
        instant.setMovingObject(apsis);
        instant.setTimestamp(apsisTime);
        instant.setCartesianState(cartesianState);
        instant.setKeplerianElements(keplerianElements);

        return instant;
    }
}
