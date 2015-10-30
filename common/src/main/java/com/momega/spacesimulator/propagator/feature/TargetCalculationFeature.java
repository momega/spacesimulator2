package com.momega.spacesimulator.propagator.feature;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.dynamic.ReferenceFrameFactory;
import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.KeplerianElements;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.TargetData;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.service.CoordinateService;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.utils.CartesianUtils;

/**
 * Created by martin on 9/28/15.
 */
@Component
public class TargetCalculationFeature implements PropagatorFeature {

    private static final Logger logger = LoggerFactory.getLogger(TargetCalculationFeature.class);

    @Autowired
    private InstantManager instantManager;

    @Autowired
    private KeplerianPropagator keplerianPropagator;

    @Autowired
    private CartesianUtils cartesianUtils;

    @Autowired
    private ModelService modelService;

    @Autowired
    private CoordinateService coordinateService;

    @Autowired
    private ReferenceFrameFactory referenceFrameFactory;

    @Override
    public void calculation(Model model, Timestamp timestamp) {
        for(Spacecraft spacecraft : modelService.findAllSpacecrafts(model)) {
            if (spacecraft.getTarget()!=null) {
                CelestialBody targetBody = spacecraft.getTarget();
                Instant si = instantManager.getInstant(model, spacecraft, timestamp);
                Vector3D sNormal = cartesianUtils.getAngularMomentum(si.getCartesianState());

                Instant ti = keplerianPropagator.get(model, targetBody, timestamp);
                Vector3D tNormal = cartesianUtils.getAngularMomentum(ti.getCartesianState());

                double angle = Vector3D.angle(sNormal, tNormal);
                CartesianState relative = cartesianUtils.subtract(si.getCartesianState(), ti.getCartesianState());
                relative.setReferenceFrame(referenceFrameFactory.getFrame(targetBody.getReferenceFrameDefinition(), model, timestamp));

                TargetData targetData = new TargetData();
                targetData.setCartesianState(relative);
                targetData.setPlanesAngle(angle);
                KeplerianElements relativeKe =  coordinateService.transform(relative, timestamp);
                targetData.setKeplerianElements(relativeKe);

                double centerAngle = Vector3D.angle(ti.getCartesianState().getPosition(), si.getCartesianState().getPosition());
                targetData.setCentreAngle(centerAngle);

                si.setTargetData(targetData);
                logger.debug("target data = {}", targetData);
            }
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
