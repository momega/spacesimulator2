package com.momega.spacesimulator.builder;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.joda.time.DateTimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.dynamic.ReferenceFrameFactory;
import com.momega.spacesimulator.model.BaryCentre;
import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.KeplerianObject;
import com.momega.spacesimulator.model.KeplerianOrbit;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.PhysicalBody;
import com.momega.spacesimulator.model.ReferenceFrame;
import com.momega.spacesimulator.model.ReferenceFrameDefinition;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.service.CoordinateService;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.utils.KeplerianUtils;
import com.momega.spacesimulator.utils.RotationUtils;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * Created by martin on 7/19/15.
 */
public abstract class MovingObjectBuilder {

    private static final Logger logger = LoggerFactory.getLogger(MovingObjectBuilder.class);

    @Autowired
    private KeplerianPropagator keplerianPropagator;

    @Autowired
    private RotationUtils rotationUtils;

    @Autowired
    private KeplerianUtils keplerianUtils;

    @Autowired
    private CoordinateService coordinateService;

    @Autowired
    private InstantManager instantManager;

    @Autowired
    private ModelService modelService;

    @Autowired
    protected ReferenceFrameFactory referenceFrameFactory;

    protected Model model = new Model();

    /**
     * Builds model and returns the instance
     */
    public final Model build() {
        initModel();
        logger.info("model initialized");
        return model;
    }

    /**
     * Builds model and returns the instance
     */
    public final Model init(Timestamp timestamp) {
        for(KeplerianObject ko : modelService.findAllKeplerianObjects(model) ) {
            keplerianPropagator.compute(model, ko, timestamp);
        }
        logger.info("keplerian objects initialized");
        return model;
    }


    protected abstract void initModel();

    public KeplerianOrbit createKeplerianOrbit(ReferenceFrameDefinition referenceFrameDefinition, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, Timestamp timeOfPeriapsis, double inclination, double ascendingNode) {
        KeplerianOrbit orbit = new KeplerianOrbit();
        orbit.setReferenceFrameDefinition(referenceFrameDefinition);
        orbit.setSemimajorAxis(semimajorAxis);
        orbit.setEccentricity(eccentricity);
        orbit.setArgumentOfPeriapsis(Math.toRadians(argumentOfPeriapsis));
        orbit.setInclination(Math.toRadians(inclination));
        orbit.setAscendingNode(Math.toRadians(ascendingNode));
        orbit.setPeriod(period);
        orbit.setTimeOfPeriapsis(timeOfPeriapsis);
        orbit.setMeanMotion(2 * Math.PI / orbit.getPeriod());
        return orbit;
    }

    public KeplerianOrbit createAndSetKeplerianOrbit(KeplerianObject keplerianObject, ReferenceFrameDefinition referenceFrameDefinition, double semimajorAxis, double eccentricity, double argumentOfPeriapsis, double period, double timeOfPeriapsis, double inclination, double ascendingNode) {
        Assert.notNull(keplerianObject);
        Assert.notNull(referenceFrameDefinition);

        Timestamp t = TimeUtils.fromJulianDay(timeOfPeriapsis);
        KeplerianOrbit orbit = createKeplerianOrbit(referenceFrameDefinition, semimajorAxis, eccentricity, argumentOfPeriapsis, period * DateTimeConstants.SECONDS_PER_DAY, t, inclination, ascendingNode);
        keplerianObject.setKeplerianOrbit(orbit);
        return orbit;
    }

    public void updateMovingObject(PhysicalBody physicalBody, double mass) {
        physicalBody.setMass(mass * 1E24);
    }

    public void updateMovingObject(CelestialBody celestialBody, double mass, double radius, double rotationPeriod, double primeMeridian, double ra, double dec) {
        updateMovingObject(celestialBody, mass);
        celestialBody.setPrimeMeridian(Math.toRadians(primeMeridian));
        celestialBody.setRotationPeriod(rotationPeriod * DateTimeConstants.SECONDS_PER_DAY);
        celestialBody.setRadius(radius * 1E6);
        celestialBody.setRa(Math.toRadians(ra));
        celestialBody.setDec(Math.toRadians(dec));
        Rotation axialTilt = rotationUtils.getAxialTilt(celestialBody.getRa(), celestialBody.getDec(), celestialBody.getPrimeMeridian(), true);
        celestialBody.setAxialTilt(axialTilt);
    }

    public void addToBaryCentre(BaryCentre baryCentre, PhysicalBody physicalBody) {
        baryCentre.getPhysicalBodies().add(physicalBody);
    }

    public void insertKeplerianObject(KeplerianObject keplerianObject) {
        model.getMovingObjects().add(keplerianObject);
    }

    public CartesianState constructCartesianState(CelestialBody celestialBody, Spacecraft spacecraft, Timestamp timestamp, double r, double theta, double inclination, double ascendingNode, double argumentOfPeriapsis, double velocity) {
        Vector3D position = keplerianUtils.getCartesianPosition(r, Math.toRadians(theta), Math.toRadians(inclination), Math.toRadians(ascendingNode), Math.toRadians(argumentOfPeriapsis));
        Rotation rotation = celestialBody.getAxialTilt();
        Vector3D top = rotationUtils.getAxisVector(rotation, Vector3D.PLUS_K);
        Vector3D v = position.crossProduct(top).normalize().scalarMultiply(velocity).negate();

        CartesianState cartesianState = new CartesianState();
        cartesianState.setPosition(position);
        cartesianState.setVelocity(v);

        ReferenceFrame referenceFrame = referenceFrameFactory.getFrame(celestialBody.getReferenceFrameDefinition(), model, timestamp);
        cartesianState.setReferenceFrame(referenceFrame);

        return cartesianState;
    }

}
