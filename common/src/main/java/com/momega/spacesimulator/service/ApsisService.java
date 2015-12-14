package com.momega.spacesimulator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.Apsis;
import com.momega.spacesimulator.model.ApsisType;
import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.utils.KeplerianUtils;

/**
 * Created by martin on 9/28/15.
 */
@Component
public class ApsisService {

    @Autowired
    private CoordinateService coordinateService;

    @Autowired
    private KeplerianUtils keplerianUtils;

    public Instant getApsis(Model model, ApsisType apsisType, KeplerianElements keplerianElements, Timestamp timestamp) {
        Apsis apsis = new Apsis();
        apsis.setType(apsisType);

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
