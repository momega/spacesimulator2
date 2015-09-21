package com.momega.spacesimulator.propagator.model;

import com.momega.spacesimulator.common.CoordinateModels;
import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.service.ModelService;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The force model of the gravitation
 * Created by martin on 8/16/14.
 */
@Component
public class GravityModel {

    private static final Logger logger = LoggerFactory.getLogger(GravityModel.class);

    @Autowired
    private ModelService modelService;

    @Autowired
    private InstantManager instantManager;

    @Autowired
    private KeplerianPropagator keplerianPropagator;

    @Autowired
    private CoordinateModels coordinateModels;

    /**
     * Computes the total gravitational force (acceleration) from all celestial bodies in the system for the defined instant.
     * @param model the model
     * @param timestamp the timestamp
     * @return total acceleration/force
     */
    public Vector3D getAcceleration(Model model, Vector3D position, ReferenceFrame referenceFrame, Timestamp timestamp) {
        Vector3D a = Vector3D.ZERO;
        for(KeplerianObject obj : modelService.findAllKeplerianObjects(model)) {
            Instant instant = instantManager.getInstant(model, obj, timestamp);
            if (instant == null) {
                instant = keplerianPropagator.compute(model, obj, timestamp);
            }
            if (obj.getName().equals("Earth")) {
                Vector3D bodyPosition = coordinateModels.getPositionInRootReferenceFrame(instant.getCartesianState().getPosition(), instant.getCartesianState().getReferenceFrame());
                Vector3D craftPosition = coordinateModels.getPositionInRootReferenceFrame(position, referenceFrame);

                //Vector3D bodyPosition = instant.getCartesianState().getPosition();
                //Vector3D craftPosition = position;

                Vector3D r = bodyPosition.subtract(craftPosition);
                double dist3 = r.getNormSq() * r.getNorm();
                a = a.add(obj.getGravitationParameter() / dist3, r); // a(i) = a(i) + G*M * r(i) / r^3
            }
        }

        logger.debug("acceleration={}", a.getNorm());

        return a;
    }

}
