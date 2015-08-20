package com.momega.spacesimulator.propagator;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.RotatingObject;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.MathUtils;
import com.momega.spacesimulator.utils.TimeUtils;
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

	public void compute(MovingObject movingObject, Timestamp newTimestamp) {
		RotatingObject rotatingObject = (RotatingObject) movingObject;
		double dt = newTimestamp.subtract(TimeUtils.JD2000);
        double phi = dt / rotatingObject.getRotationPeriod() * 2 * Math.PI;
        phi = MathUtils.normalizeAngle(phi);

        logger.debug("phi = {}", phi);


        //TODO: implement this
        //rotatingObject.setPrimeMeridian(rotatingObject.getPrimeMeridianJd2000() + phi);
	}

}
