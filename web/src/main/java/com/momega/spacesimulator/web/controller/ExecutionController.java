package com.momega.spacesimulator.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.momega.spacesimulator.simulation.Simulation;
import com.momega.spacesimulator.web.service.SimulationHolder;

@RestController
@RequestMapping(value="/api/execution")
public class ExecutionController {
	
	@Autowired
	private SimulationHolder simulationHolder;
	
	@Autowired
	private SimulationTransformer simulationTransformer;
	
	@Autowired
	@Qualifier("simulationsTaskExecutor")
	private ThreadPoolTaskExecutor simulationsTaskExecutor;
	
	@Autowired
	@Qualifier("solversTaskExecutor")
	private ThreadPoolTaskExecutor solversTaskExecutor;

	@ResponseBody
	@RequestMapping(value = "/{uuid}", method = {RequestMethod.PUT})
	public SimulationDto runSimulation(@PathVariable("uuid") String uuid) {
		Simulation<?, ?> simulation = simulationHolder.findSimulation(uuid);
		if (simulation == null) {
			throw new IllegalArgumentException("unknown simulation with uuid " + uuid);
		}
		if (simulation.isRunning()) {
			throw new IllegalArgumentException("simulation uuid " + uuid + " is already running");
		}
		simulation.setTaskExecutor(solversTaskExecutor);
		// execute the simulation
		simulationsTaskExecutor.submit(simulation);
		
		SimulationDto result = simulationTransformer.transform(simulation);
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/{uuid}", method = {RequestMethod.DELETE})
	public SimulationDto stopSimulation(@PathVariable("uuid") String uuid) {
		Simulation<?, ?> simulation = simulationHolder.findSimulation(uuid);
		if (simulation == null) {
			throw new IllegalArgumentException("unknown simulation with uuid " + uuid);
		}
		if (!simulation.isRunning()) {
			throw new IllegalArgumentException("simulation uuid " + uuid + " is not running");
		}
		
		simulation.stop();
		
		SimulationDto result = simulationTransformer.transform(simulation);
		return result;
	}
}
