package com.momega.spacesimulator.builder;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.CelestialBody;

/**
 * Created by martin on 10/19/15.
 */
@Component
@Scope("prototype")
public class EarthMoonBuilder extends MovingObjectBuilder {

    @Override
    protected void buildModel() {
        CelestialBody earth = new CelestialBody();
        earth.setName("Earth");
        createReferenceFrameDefinition(earth);
        updateMovingObject(earth, 5.97219, 6.378, 0.997269, 190.147d, 0d, 90d);

        CelestialBody moon = new CelestialBody();
        moon.setName("Moon");
        updateMovingObject(moon, 0.07349, 1.737, 27.321, 38.3213d, 269.9949d, 66.5392d);
        createAndSetKeplerianOrbit(moon, earth, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.145, 208.1199);

        insertKeplerianObject(earth);
        insertKeplerianObject(moon);
    }

}
