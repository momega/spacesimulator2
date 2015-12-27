package com.momega.spacesimulator.simulation.test;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.momega.spacesimulator.simulation.Simulation;
import com.momega.spacesimulator.simulation.SimulationsHolder;
import com.momega.spacesimulator.simulation.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TestSimulationTest {

	@Autowired
	private SimulationsHolder simulationsHolder;
	
	@Test
	public void runSimulation() throws InterruptedException, ExecutionException, FileNotFoundException {
		TestParameters parameters = new TestParameters();
		parameters.count = 10;
		Simulation<TestParameters, TestResult> simulation = simulationsHolder.createAndRunSimulation(TestSimulation.class, parameters);
		Future<List<TestResult>> f = simulationsHolder.getFuture(simulation);
		List<TestResult> list = f.get();
		Assert.assertNotNull(list);
		Assert.assertEquals(10, list.size());
	}

}
