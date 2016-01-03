/**
 * 
 */
package com.momega.spacesimulator.simulation;

import java.util.*;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author martin
 *
 */
@Component
public class SimulationFactory {

	private Map<Simulation<?, ?>, Future<?>> futures = new HashMap<>();

	@Autowired
    private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
	private ApplicationContext applicationContext;

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

    public Simulation<?, ?> createSimulation(String name) {
        Simulation<?, ?> simulation = applicationContext.getBean(name, Simulation.class);
        return simulation;
    }

    public <P, I> Simulation<P, I> createSimulation(Class<? extends Simulation<P, I>> clazz) {
        Simulation<P, I> simulation = applicationContext.getBean(clazz);
        return simulation;
    }

	public <P, I> Simulation<P, I> createAndRunSimulation(Class<? extends Simulation<P, I>> clazz, P parameters) {
        Simulation<P, I> simulation = createSimulation(clazz);
        runSimulation(simulation, parameters);
        return simulation;
	}

    public <P, I> Simulation<P, I> runSimulation(Simulation<P, I> simulation, P parameters) {
        Assert.notNull(simulation);
        Assert.notNull(parameters);
        simulation.setFields(parameters);
        Future<List<I>> f = taskExecutor.submit(simulation);
        futures.put(simulation, f);
        return simulation;
    }
	
	public <P, I> Future<List<I>> getFuture(Simulation<P, I> simulation) {
		return (Future<List<I>>) futures.get(simulation);
	}
	
	public Map<Simulation<?, ?>, Future<?>> getFutures() {
		return Collections.unmodifiableMap(futures);
	}


}
