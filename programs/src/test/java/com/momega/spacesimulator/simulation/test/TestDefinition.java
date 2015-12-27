package com.momega.spacesimulator.simulation.test;

import com.momega.spacesimulator.simulation.Simulation;
import com.momega.spacesimulator.simulation.SimulationDefinition;

/**
 * Created by martin on 12/27/15.
 */
public class TestDefinition extends SimulationDefinition {

    protected TestDefinition() {
        super("Test", TestSimulation.class, TestParameters.class);
    }
}
