package com.momega.spacesimulator.model;

import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.propagator.KeplerianPropagator;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.service.PropagationResult;
import com.momega.spacesimulator.utils.TimeUtils;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 8/11/15.
 */
public class PropagateTest {

    @Test
    public void propagateTest() {
        InstantManager instantManager = new InstantManager();
        Model model = new Model();
        CelestialBody celestialBody = new CelestialBody();
        celestialBody.setName("X");

        MockKeplerianPropagator keplerianPropagator = new MockKeplerianPropagator();
        keplerianPropagator.setInstantManager(instantManager);

        ModelService modelService = new ModelService();
        modelService.setInstantManager(instantManager);

        modelService.setKeplerianPropagator(keplerianPropagator);

        List<MovingObject> list = new ArrayList<>();
        list.add(celestialBody);

        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2015, 9, 23, 12, 0, DateTimeZone.UTC));
        TimeInterval timeInterval = TimeUtils.createInterval(timestamp, 5);

        PropagationResult result = modelService.propagateTrajectories(model, list, timeInterval, 1.0);

        Instant instant = instantManager.getInstant(model, celestialBody, timestamp);
        Assert.assertNull(instant);

        instant = instantManager.getInstant(model, celestialBody, timeInterval.getEndTime());
        Assert.assertNotNull(instant);

        Assert.assertNotNull(model.getInstants());
        Assert.assertEquals(6, model.getInstants().size());

        Assert.assertNotNull(result);
        Assert.assertEquals(timestamp, result.getStartTime());
        Assert.assertNotNull(result.getInstants());
        Assert.assertEquals(1, result.getInstants().size());
        Assert.assertNotNull(result.getInstants().get(celestialBody));

    }

    public class MockKeplerianPropagator extends KeplerianPropagator {

        private InstantManager instantManager;

        @Override
        public Instant compute(Model model, KeplerianObject keplerianObject, Timestamp newTimestamp) {
            Instant instant = instantManager.newInstant(model, keplerianObject, new CartesianState(), new KeplerianElements(), newTimestamp);
            return instant;
        }

        public void setInstantManager(InstantManager instantManager) {
            this.instantManager = instantManager;
        }
    }
}
