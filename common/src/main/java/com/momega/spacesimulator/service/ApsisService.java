package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 9/28/15.
 */
@Component
public class ApsisService {

    @Autowired
    private CoordinateService coordinateService;

    public Instant getApsis(Model model, ApsisType apsisType, KeplerianOrbit keplerianOrbit, Timestamp timestamp) {
        Apsis apsis = new Apsis();
        apsis.setType(apsisType);

        KeplerianElements keplerianElements = new KeplerianElements();
        keplerianElements.setTrueAnomaly(apsisType.getTrueAnomaly());
        keplerianElements.setKeplerianOrbit(keplerianOrbit);

        CartesianState cartesianState = coordinateService.transform(model, timestamp, keplerianElements);

        Instant instant = new Instant();
        instant.setMovingObject(apsis);
        instant.setCartesianState(cartesianState);
        instant.setKeplerianElements(keplerianElements);

        return instant;
    }
}
