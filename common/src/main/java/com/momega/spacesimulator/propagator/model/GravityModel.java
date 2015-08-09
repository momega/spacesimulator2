package com.momega.spacesimulator.propagator.model;

import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.service.ModelService;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 7/21/15.
 */
@Component
public class GravityModel {

    @Autowired
    private ModelService modelService;

    @Autowired
    private InstantManager instantManager;

    public Vector3D getAcceleration(Model model, Instant spacecraft) {
        Vector3D position = spacecraft.getCartesianState().getPosition();
        Vector3D a = Vector3D.ZERO;
        for(CelestialBody celestialBody : modelService.findAllCelestialBodies(model)) {
            Instant instant = instantManager.getInstant(model, celestialBody, spacecraft.getTimestamp());
            Vector3D r = instant.getCartesianState().getPosition().subtract(position);
            double dist3 = r.getNormSq() * r.getNorm();
            a = a.add(celestialBody.getGravitationParameter() / dist3, r); // a(i) = a(i) + G*M * r(i) / r^3
        }
        return a;
    }

}
