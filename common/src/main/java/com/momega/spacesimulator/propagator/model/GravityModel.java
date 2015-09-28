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
    private KeplerianPropagator keplerianPropagator;

    @Autowired
    private CoordinateModels coordinateModels;

    private GravityFilter gravityFilter;

    /**
     * Computes the total gravitational force (acceleration) from all celestial bodies in the system for the defined instant.
     * @param model the model
     * @param timestamp the timestamp
     * @return total acceleration/force
     */
    public Vector3D getAcceleration(Model model, CartesianState theState, Timestamp timestamp) {
        Vector3D a = Vector3D.ZERO;
        logger.debug("time = {}", timestamp);
        for(KeplerianObject obj : modelService.findAllKeplerianObjects(model)) {
            if (gravityFilter.filter(obj)) {
                Instant instant = keplerianPropagator.get(model, obj, timestamp);
                logger.debug("Earth = {}", traceCartesianState(instant.getCartesianState()));
                logger.debug("SpaceCraft = {}", traceCartesianState(theState));

                Vector3D bodyPosition = coordinateModels.transferToRoot(instant.getCartesianState()).getPosition();
                Vector3D craftPosition = coordinateModels.transferToRoot(theState).getPosition();

                Vector3D r = bodyPosition.subtract(craftPosition);

                logger.debug("r={}", r.getNorm());
                double dist3 = r.getNormSq() * r.getNorm();
                a = a.add(obj.getGravitationParameter() / dist3, r); // a(i) = a(i) + G*M * r(i) / r^3
            }
        }

        logger.debug("acceleration={}", a.getNorm());

        return a;
    }

    protected String traceCartesianState(CartesianState cartesianState) {
        StringBuilder sb = new StringBuilder();
        sb.append("[C=" + cartesianState.getPosition().getNorm() + "] ");
        sb.append(traceReferenceFrame(cartesianState.getReferenceFrame()));
        return sb.toString();
    }

    protected String traceReferenceFrame(ReferenceFrame referenceFrame) {
        StringBuilder sb = new StringBuilder();
        sb.append("[" + referenceFrame.getDefinition().getKeplerianObject().getName() + " ");
        sb.append(referenceFrame.getCartesianState().getPosition().getNorm() + " ");
        sb.append(referenceFrame.getTimestamp() + "]");
        if (referenceFrame.getParent()!=null) {
            sb.append(traceReferenceFrame(referenceFrame.getParent()));
        }
        return sb.toString();
    }

    public void setGravityFilter(GravityFilter gravityFilter) {
        this.gravityFilter = gravityFilter;
    }
}
