package com.momega.spacesimulator.propagator.feature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.TargetData;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.ModelService;

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
