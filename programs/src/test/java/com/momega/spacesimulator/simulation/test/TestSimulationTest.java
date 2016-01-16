package com.momega.spacesimulator.simulation.test;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.momega.spacesimulator.simulation.SimulationFactory;
import com.momega.spacesimulator.simulation.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TestSimulationTest {

	@Autowired
	private SimulationFactory simulationFactory;
	
	@Test
	public void runSimulation() throws InterruptedException, ExecutionException, FileNotFoundException {
		TestFields parameters = new TestFields();
		parameters.count = 10;
		List<TestResult> list = simulationFactory.createAndRunSimulation(TestSimulation.class, parameters);
		Assert.assertNotNull(list);
		Assert.assertEquals(10, list.size());
	}

}
