package com.momega.spacesimulator.propagator.feature;

import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 10/19/15.
 */
@Component
public class MinimalTargetCalculationFeature implements PropagatorFeature {

    @Autowired
    private InstantManager instantManager;

    @Autowired
    private ModelService modelService;

    @Override
    public void calculation(Model model, Timestamp timestamp) {
        for(Spacecraft spacecraft : modelService.findAllSpacecrafts(model)) {
            if (spacecraft.getTarget() != null && spacecraft.getThreshold()>0) {
                CelestialBody targetBody = spacecraft.getTarget();
                Instant si = instantManager.getInstant(model, spacecraft, timestamp);
                TargetData targetData = si.getTargetData();

                double dist = targetData.getCartesianState().getPosition().getNorm();
                if (dist < spacecraft.getThreshold() && dist < spacecraft.getMinimalDistance()) {
                    spacecraft.setMinimalDistance(dist);
                    spacecraft.setMinimalInstant(si);
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }

}
