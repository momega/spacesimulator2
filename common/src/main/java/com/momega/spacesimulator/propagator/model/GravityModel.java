package com.momega.spacesimulator.propagator.model;

import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.service.ModelService;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The force model of the gravitation
 * Created by martin on 8/16/14.
 */
@Component
public class GravityModel {

    @Autowired
    private ModelService modelService;

    @Autowired
    private InstantManager instantManager;

    @Autowired
    private KeplerianPropagator keplerianPropagator;

    /**
     * Computes the total gravitational force (acceleration) from all celestial bodies in the system for the defined instant.
     * @param model the model
     * @param timestamp the timestamp
     * @return total acceleration/force
     */
    public Vector3D getAcceleration(Model model, Vector3D position, Timestamp timestamp) {
        Vector3D a = Vector3D.ZERO;
        for(CelestialBody celestialBody : modelService.findAllCelestialBodies(model)) {
            Instant instant = instantManager.getInstant(model, celestialBody, timestamp);
            if (instant == null) {
                instant = keplerianPropagator.compute(model, celestialBody, timestamp);
            }
            Vector3D r = instant.getCartesianState().getPosition().subtract(position);
            double dist3 = r.getNormSq() * r.getNorm();
            a = a.add(celestialBody.getGravitationParameter() / dist3, r); // a(i) = a(i) + G*M * r(i) / r^3
        }
        return a;
    }

}
