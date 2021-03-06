package com.momega.spacesimulator.service.builder;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.BaryCentre;
import com.momega.spacesimulator.model.CelestialBody;

/**
 * Created by martin on 10/23/15.
 */
@Component
@Scope("prototype")
public class SunEarthMoonBuilder extends MovingObjectBuilder {

    @Override
    protected void buildModel() {
        CelestialBody sun = new CelestialBody();
        sun.setName("Sun");
        updateMovingObject(sun, 1.989 * 1E6, 696.342, 25.05, 0d, 286.13, 63.87);
        createReferenceFrameDefinition(sun);

        BaryCentre earthMoonBarycenter = new BaryCentre();
        earthMoonBarycenter.setName("Earth-Moon Barycenter");
        createAndSetKeplerianOrbit(earthMoonBarycenter, sun, 149598.261d * 1E6, 0.0166739, 287.5824, 365.256814, 2456661.138788696378, 0.0018601064, 175.395d);

        CelestialBody earth = new CelestialBody();
        earth.setName("Earth");
        updateMovingObject(earth, 5.97219, 6.378, 0.997269, 0d, 90d, 190.147d);

        CelestialBody moon = new CelestialBody();
        moon.setName("Moon");
        updateMovingObject(moon, 0.07349, 1.737, 27.321, 269.9949, 66.5392, 38.3213);

        addToBaryCentre(earthMoonBarycenter, earth);
        addToBaryCentre(earthMoonBarycenter, moon);

        createAndSetKeplerianOrbit(earth, earthMoonBarycenter, 4.686955382086 * 1E6, 0.055557, 264.7609, 27.427302, 2456796.39770, 5.241500, 208.1199);
        createAndSetKeplerianOrbit(moon, earthMoonBarycenter, 384.399 * 1E6, 0.055557, 84.7609, 27.427302, 2456796.39770989, 5.241500, 208.1199);

        insertKeplerianObject(sun);
        insertKeplerianObject(earthMoonBarycenter);
        insertKeplerianObject(earth);
        insertKeplerianObject(moon);
    }

}
