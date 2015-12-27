package com.momega.spacesimulator.simulation.moonorbit;

import org.springframework.stereotype.Component;

import com.momega.spacesimulator.simulation.SimulationDefinition;

/**
 * Created by martin on 12/27/15.
 */
@Component
public class MoonOrbitDefinition extends SimulationDefinition {

    protected MoonOrbitDefinition() {
        super("Moon Orbit", MoonOrbitSimulation.class, MoonOrbitParameters.class);
    }
}
