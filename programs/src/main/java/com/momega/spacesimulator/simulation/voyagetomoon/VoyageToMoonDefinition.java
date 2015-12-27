package com.momega.spacesimulator.simulation.voyagetomoon;

import com.momega.spacesimulator.simulation.SimulationDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by martin on 12/27/15.
 */
@Component
@Scope("prototype")
public class VoyageToMoonDefinition extends SimulationDefinition {

    protected VoyageToMoonDefinition() {
        super("Voyage To Moon", VoyageToMoonSimulation.class, VoyageToMoonParameters.class);
    }
}
