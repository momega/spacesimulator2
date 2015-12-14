/**
 * 
 */
package com.momega.spacesimulator.simulation.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.simulation.SimulationCallable;

/**
 * @author martin
 *
 */
@Component
@Scope("prototype")
public class TestSimulationCallable extends SimulationCallable<TestSimulationResult> {
	
	private static final Logger logger = LoggerFactory.getLogger(TestSimulationCallable.class);

	@Override
	public TestSimulationResult apply(TestSimulationResult result) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// do nothing
		}
		result.setFinished(true);
		logger.warn("result = {}", result);
		return result;
	}

}
