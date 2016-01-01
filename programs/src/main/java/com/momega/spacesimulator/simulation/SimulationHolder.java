package com.momega.spacesimulator.simulation;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by martin on 12/31/15.
 */
@Component
public class SimulationHolder {

    private Map<String, Simulation<?,?>> simulations = new HashMap<>();

    public void addSimulation(Simulation<?, ?> simulation) {
        simulations.put(simulation.getUuid(), simulation);
    }

    public Simulation<?, ?> findSimulation(String uuid) {
        return simulations.get(uuid);
    }

    public List<Simulation<?, ?>> getSimulations() {
        return Collections.unmodifiableList(new ArrayList<>(simulations.values()));
    }

    public void clearSimulations() {
        simulations.clear();
    }

}
