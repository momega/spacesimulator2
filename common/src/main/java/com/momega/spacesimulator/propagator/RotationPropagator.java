package com.momega.spacesimulator.propagator;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Instant;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * Rotation propagator is used to 
 * rotate the {@link CelestialBody} such as {@link CelestialBody}.
 * It supports only RotatingObject.
 * Created by martin on 5/25/14.
 */
@Component
public class RotationPropagator {

    private static final Logger logger = LoggerFactory.getLogger(RotationPropagator.class);

	public void compute(CelestialBody celestialBody, Instant instant, Timestamp newTimestamp) {
		double dt = newTimestamp.subtract(TimeUtils.JD2000);
        double phi = dt / celestialBody.getRotationPeriod() * 2 * Math.PI;
        phi = MathUtils.normalizeAngle(phi);

        logger.debug("phi = {}", phi);

        Rotation r = new Rotation(Vector3D.PLUS_K, phi);
        r = celestialBody.getAxialTilt().applyTo(r);

        instant.setRotation(r);
	}

}
