package com.momega.spacesimulator.simulation;

import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, String> getParametersDefinition() {
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(parametersClass);
        Map<String, String> result = new HashMap<>();
        for(PropertyDescriptor d : descriptors) {
            result.put(d.getName(), d.getPropertyType().getName());
        }
        return result;
    }
}
