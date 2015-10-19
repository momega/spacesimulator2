package com.momega.spacesimulator.builder;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.ReferenceFrameDefinition;
import com.momega.spacesimulator.model.Timestamp;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 10/19/15.
 */
@Component
@Scope("prototype")
public class EarthMoonBuilder extends MovingObjectBuilder {

    @Override
    protected void initModel() {
        CelestialBody earth = new CelestialBody();
        earth.setName("Earth");
        ReferenceFrameDefinition rootDefinition = referenceFrameFactory.createDefinition(earth, null);
        model.setRootReferenceFrameDefinition(rootDefinition);

        updateMovingObject(earth, 5.97219, 6.378, 0.997269, 190.147d, 0d, 90d);

        CelestialBody moon = new CelestialBody();
        moon.setName("Moon");
        updateMovingObject(moon, 0.07349, 1.737, 27.321, 38.3213d, 269.9949d, 66.5392d);
        createAndSetKeplerianOrbit(moon, rootDefinition, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.145, 208.1199);
        referenceFrameFactory.createDefinition(moon, rootDefinition);

        insertKeplerianObject(earth);
        insertKeplerianObject(moon);
    }

}
