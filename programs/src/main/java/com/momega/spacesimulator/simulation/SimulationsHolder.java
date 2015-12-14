/**
 * 
 */
package com.momega.spacesimulator.simulation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author martin
 *
 */
@Component
public class SimulationsHolder {

	private Map<Simulation<?, ?>, Future<?>> futures = new HashMap<>();

	@Autowired
    private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public <P, I> Simulation<P, I> createSimulation(Class<? extends Simulation<P, I>> clazz, P parameters) {
		Simulation<P, I> simulation = applicationContext.getBean(clazz, parameters);
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
