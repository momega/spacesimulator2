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

    public Map<String, PropertyDescriptor> getPropertyDescriptors() {
        PropertyDescriptor[] descriptors = BeanUtils.getPropertyDescriptors(parametersClass);
        Map<String, PropertyDescriptor> result = new HashMap<>();
        for(PropertyDescriptor d : descriptors) {
            if (!d.getName().equals("class")) {
                result.put(d.getName(), d);
            }
        }
        return result;
    }

    public Map<String, String> getParametersDefinition() {
        Map<String, PropertyDescriptor> propertyDescriptors = getPropertyDescriptors();
        Map<String, String> result = new HashMap<>();
        for(Map.Entry<String, PropertyDescriptor> entry : propertyDescriptors.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getPropertyType().getName());
        }
        return result;
    }

	@Override
	public String toString() {
		return "SimulationDefinition [name=" + name + ", simulationClass="
				+ simulationClass + ", parametersClass=" + parametersClass
				+ "]";
	}
    
}
