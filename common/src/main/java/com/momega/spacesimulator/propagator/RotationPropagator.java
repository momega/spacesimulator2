package com.momega.spacesimulator.propagator;

import com.momega.spacesimulator.model.*;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Rotation propagator is used to 
 * rotate the {@link RotatingObject} such as {@link CelestialBody}.
 * It supports only RotatingObject.
 * Created by martin on 5/25/14.
 */
@Component
public class RotationPropagator {

    private static final Logger logger = LoggerFactory.getLogger(RotationPropagator.class);

	public void compute(MovingObject movingObject, Instant instant, Timestamp newTimestamp) {
		RotatingObject rotatingObject = (RotatingObject) movingObject;
		double dt = newTimestamp.subtract(TimeUtils.JD2000);
        double phi = dt / rotatingObject.getRotationPeriod() * 2 * Math.PI;
        phi = MathUtils.normalizeAngle(phi);

        logger.debug("phi = {}", phi);

        Rotation r = new Rotation(Vector3D.PLUS_K, phi);
        r = rotatingObject.getAxialTilt().applyTo(r);

        instant.setRotation(r);
	}

}
