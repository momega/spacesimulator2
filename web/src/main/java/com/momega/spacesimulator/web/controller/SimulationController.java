package com.momega.spacesimulator.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.momega.spacesimulator.simulation.SimulationDefinition;
import com.momega.spacesimulator.simulation.SimulationHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.momega.spacesimulator.simulation.Simulation;
import com.momega.spacesimulator.simulation.SimulationFactory;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

	private static final Logger logger = LoggerFactory.getLogger(SimulationController.class);
	
	@Autowired
	private SimulationFactory simulationFactory;

	@Autowired
	private SimulationHolder simulationHolder;

	@Autowired
	private SimulationTransformer simulationTransformer;
	
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<SimulationDto> getSimulations() {
   		Collection<Simulation<?,?>> simulations = simulationHolder.getSimulations();
		List<SimulationDto> result = new ArrayList<>();
		for(Simulation<?,?> simulation : simulations) {
			result.add(simulationTransformer.transform(simulation));
		}
		logger.info("simulations count = {}", result.size());
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
	public SimulationDto getSimulation(@PathVariable("uuid") String uuid) {
		Simulation<?, ?> simulation = simulationHolder.findSimulation(uuid);
		if (simulation == null) {
			throw new IllegalArgumentException("unknown simulation with uuid " + uuid);
		}
		Object fields = simulation.getFields();
		Assert.notNull(fields);
		SimulationDto result = simulationTransformer.transform(simulation);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/{uuid}", method = {RequestMethod.PUT, RequestMethod.POST})
	public SimulationDto updateSimulation(@RequestBody BasicSimulationDto simulationDto) {
		String uuid = simulationDto.getUuid();
		Simulation<?, ?> simulation = simulationHolder.findSimulation(uuid);
		if (simulation == null) {
			throw new IllegalArgumentException("unknown simulation with uuid " + uuid);
		}
		if (simulation.isRunning()) {
			throw new IllegalArgumentException("simulation uuid " + uuid + " is already running");
		}
		Object fields = simulation.getFields();
		Assert.notNull(fields);
		simulationTransformer.updateFields(fields, simulationDto.getFieldValues());
		SimulationDto result = simulationTransformer.transform(simulation);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
	public SimulationDto deleteSimulation(@PathVariable("uuid") String uuid) {
		Simulation<?, ?> simulation = simulationHolder.findSimulation(uuid);
		if (simulation == null) {
			throw new IllegalArgumentException("unknown simulation with uuid " + uuid);
		}
		if (simulation.isRunning()) {
			throw new IllegalArgumentException("simulation uuid " + uuid + " is already running and cannot be deleted");
		}
		simulation = simulationHolder.removeSimulation(uuid);
		SimulationDto result = simulationTransformer.transform(simulation);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	public SimulationDto newDefinition(@RequestBody BasicSimulationDto simulationDto) {
		Assert.notNull(simulationDto);
		if (StringUtils.hasText(simulationDto.getUuid())) {
			return updateSimulation(simulationDto);
		}
		Assert.notNull(simulationDto.getName());
		SimulationDefinition simulationDefinition = simulationFactory.findDefinition(simulationDto.getName());
		Assert.notNull(simulationDefinition);
		Object fields = createFieldsInstance(simulationDefinition);
		simulationTransformer.updateFields(fields, simulationDto.getFieldValues());
		Simulation<Object, Object> simulation = (Simulation<Object, Object>) simulationFactory.createSimulation(simulationDefinition.getSimulationClass());
		simulationHolder.addSimulation(simulation);
		simulation.setFields(fields);
		SimulationDto result = simulationTransformer.transform(simulation);
		return result;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleException(Exception e) {
	    return e.getMessage();
	}

	protected Object createFieldsInstance(SimulationDefinition definition) {
		try {
			Object parametersInstance = definition.getParametersClass().newInstance();
			return parametersInstance;
		} catch (Exception e) {
			throw new IllegalStateException("unable to create parameters instance ", e);
		}
	}

}
