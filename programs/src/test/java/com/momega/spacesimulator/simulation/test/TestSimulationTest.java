package com.momega.spacesimulator.simulation.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.momega.spacesimulator.simulation.SimulationFactory;
import com.momega.spacesimulator.simulation.SimulationState;
import com.momega.spacesimulator.simulation.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TestSimulationTest {

	@Autowired
	private SimulationFactory simulationFactory;
	
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor; 
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Test
	public void runSimulation() {
		TestSimulation simulation= applicationContext.getBean(TestSimulation.class);
		TestFields fields = new TestFields();
		fields.setCount(10);
		List<TestResult> list = simulationFactory.runSimulation(simulation, fields, taskExecutor);
		Assert.assertNotNull(list);
		Assert.assertEquals(10, list.size());
	}
	
	@Test
	public void checkFinish() {
		TestFields fields = new TestFields();
		fields.setCount(10);
		TestSimulation simulation = applicationContext.getBean(TestSimulation.class);
		simulation.setFields(fields);
		simulation.setTaskExecutor(taskExecutor);
		List<TestResult> list = simulation.call();
		Assert.assertNotNull(list);
		Assert.assertEquals(fields.getCount(), simulation.getCompletedInputs());
		Assert.assertEquals(fields.getCount(), simulation.getTotalInputs());
		Assert.assertEquals(0, simulation.getFailedInputs());
		Assert.assertEquals(SimulationState.FINISHED, simulation.getSimulationState());
		Assert.assertNotNull(simulation.getStartedAt());
		Assert.assertNotNull(simulation.getFinishedAt());
		Assert.assertTrue(simulation.getFinishedAt().after(simulation.getStartedAt()));
	}

}
