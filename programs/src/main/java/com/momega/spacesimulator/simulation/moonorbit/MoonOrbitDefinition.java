package com.momega.spacesimulator.simulation.moonorbit;

import com.momega.spacesimulator.simulation.SimulationDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 12/27/15.
 */
@Component
@Scope("prototype")
public class MoonOrbitDefinition extends SimulationDefinition {

    protected MoonOrbitDefinition() {
        super("Moon Orbit", MoonOrbitSimulation.class, MoonOrbitParameters.class);
    }
}
