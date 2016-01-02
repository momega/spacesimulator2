package com.momega.spacesimulator.simulation;

/**
 * Created by martin on 12/27/15.
 */
public abstract class SimulationDefinition {

    private final String name;
    private final Class<? extends Simulation<?, ?>> simulationClass;
    private final Class<?> parametersClass;

    protected SimulationDefinition(String name, Class<? extends Simulation<?,?>> simulationClass, Class<?> parametersClass) {
        this.name = name;
        this.simulationClass = simulationClass;
        this.parametersClass = parametersClass;
    }

    public Class<? extends Simulation<?, ?>> getSimulationClass() {
        return simulationClass;
    }

    public String getName() {
        return name;
    }

    public Class<?> getParametersClass() {
        return parametersClass;
    }

	@Override
	public String toString() {
		return "SimulationDefinition [name=" + name + ", simulationClass="
				+ simulationClass + ", parametersClass=" + parametersClass
				+ "]";
	}
    
}
