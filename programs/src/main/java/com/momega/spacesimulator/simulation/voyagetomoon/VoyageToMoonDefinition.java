package com.momega.spacesimulator.simulation.voyagetomoon;

import org.springframework.stereotype.Component;

import com.momega.spacesimulator.simulation.SimulationDefinition;

/**
 * Created by martin on 12/27/15.
 */
@Component
public class VoyageToMoonDefinition extends SimulationDefinition {

    protected VoyageToMoonDefinition() {
        super("Voyage To Moon", VoyageToMoonSimulation.class, VoyageToMoonFields.class);
    }
}
