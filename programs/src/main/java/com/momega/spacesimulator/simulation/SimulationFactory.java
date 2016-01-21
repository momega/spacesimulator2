/**
 * 
 */
package com.momega.spacesimulator.simulation;

import java.util.List;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author martin
 *
 */
@Component
public class SimulationFactory {

	public <P, I> List<I> runSimulation(Simulation<P, I> simulation, P fields, AsyncTaskExecutor taskExecutor) {
        Assert.notNull(simulation);
        Assert.notNull(fields);
        simulation.setTaskExecutor(taskExecutor);
        simulation.setFields(fields);
        List<I> outputs = simulation.call();
        return outputs;
    }

}
