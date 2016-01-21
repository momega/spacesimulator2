package com.momega.spacesimulator.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.momega.spacesimulator.simulation.Simulation;
import com.momega.spacesimulator.simulation.SimulationDefinition;

@Component
public class DefinitionService {

	@Autowired
	private ApplicationContext applicationContext;
	
	public Simulation<?,?> createSimulation(Class<?> clazz) {
		return (Simulation<?,?>) applicationContext.getBean(clazz);
	}

    public List<SimulationDefinition> getDefinitions() {
        Map<String, SimulationDefinition> defs = applicationContext.getBeansOfType(SimulationDefinition.class);
        return new ArrayList<>(defs.values());
    }

    public SimulationDefinition findDefinition(String name) {
    	Assert.notNull(name);
    	List<SimulationDefinition> defs = getDefinitions();
        for(SimulationDefinition def : defs) {
        	if (name.equals(def.getName())) {
        		return def;
        	}
        }
        return null;
    }

}
