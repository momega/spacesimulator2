package com.momega.spacesimulator.simulation.test;

import com.momega.spacesimulator.simulation.SimulationDefinition;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 12/27/15.
 */
@Component
public class TestDefinition extends SimulationDefinition {

    public TestDefinition() {
        super("Test", TestSimulation.class, TestFields.class);
    }
}
