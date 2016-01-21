/**
 * 
 */
package com.momega.spacesimulator.simulation.test;

import com.momega.spacesimulator.simulation.SimulationSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author martin
 *
 */
@Component
@Scope("prototype")
public class TestSolver extends SimulationSolver<TestResult> {
	
	private static final Logger logger = LoggerFactory.getLogger(TestSolver.class);

	@Override
	public TestResult apply(TestResult result) {
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
