package com.momega.spacesimulator.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.momega.spacesimulator.simulation.Simulation;
import com.momega.spacesimulator.simulation.SimulationDefinition;
import com.momega.spacesimulator.simulation.SimulationFactory;
import com.momega.spacesimulator.web.service.DefinitionService;
import com.momega.spacesimulator.web.service.SimulationHolder;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {

	private static final Logger logger = LoggerFactory.getLogger(SimulationController.class);
	
	@Autowired
	private SimulationFactory simulationFactory;

	@Autowired
	private SimulationHolder simulationHolder;
	
	@Autowired
	private DefinitionService definitionService;
	
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private SimulationTransformer simulationTransformer;
	
	@ResponseBody
	@RequestMapping(value = "example", method = RequestMethod.GET)
	public List<SimulationDto> createExamples() {
		BasicSimulationDto voyageToMoon = new BasicSimulationDto();
		voyageToMoon.setName("Voyage To Moon");
		voyageToMoon.getFieldValues().add(createField("endSpeed", FieldType.INT, "10843"));
		voyageToMoon.getFieldValues().add(createField("startSpeed", FieldType.INT, "10839"));
		voyageToMoon.getFieldValues().add(createField("startTime", FieldType.TIMESTAMP, "2014-09-12T15:50:00.000"));
		voyageToMoon.getFieldValues().add(createField("endTime", FieldType.TIMESTAMP, "2014-09-12T16:00:00.000"));
		voyageToMoon.getFieldValues().add(createField("stepInSeconds", FieldType.DOUBLE, "10.0"));
		
		BasicSimulationDto moonOrbit = new BasicSimulationDto();
		moonOrbit.setName("Moon Orbit");
		moonOrbit.getFieldValues().add(createField("startTime", FieldType.TIMESTAMP, "2014-09-12T15:40:00.000"));
		moonOrbit.getFieldValues().add(createField("endTime", FieldType.TIMESTAMP, "2014-09-12T15:59:00.000"));
		moonOrbit.getFieldValues().add(createField("speed", FieldType.DOUBLE, "10841.0"));
		moonOrbit.getFieldValues().add(createField("startBurnTime", FieldType.TIMESTAMP, "2014-09-16T06:45:00.000"));
		moonOrbit.getFieldValues().add(createField("endBurnTime", FieldType.TIMESTAMP, "2014-09-16T06:52:00.000"));
		moonOrbit.getFieldValues().add(createField("stepTime", FieldType.DOUBLE, "10.0"));
		moonOrbit.getFieldValues().add(createField("burnTime", FieldType.DOUBLE, "362.0"));
    	
		List<SimulationDto> list = new ArrayList<>();
		list.add(newSimulation(voyageToMoon));
		list.add(newSimulation(moonOrbit));
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<SimulationDto> querySimulations() {
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
	@RequestMapping(value = "/{uuid}", method = {RequestMethod.PUT})
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
		logger.info("simulation {} updated", uuid);
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
	public SimulationDto newSimulation(@RequestBody BasicSimulationDto simulationDto) {
		Assert.notNull(simulationDto);
		if (StringUtils.hasText(simulationDto.getUuid())) {
			return updateSimulation(simulationDto);
		}
		Assert.notNull(simulationDto.getName());
		SimulationDefinition simulationDefinition = definitionService.findDefinition(simulationDto.getName());
		Assert.notNull(simulationDefinition);
		Object fields = createFieldsInstance(simulationDefinition);
		simulationTransformer.updateFields(fields, simulationDto.getFieldValues());
		Simulation<Object, Object> simulation = (Simulation<Object, Object>) definitionService.createSimulation(simulationDefinition.getSimulationClass());
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
	
	protected FieldValueDto createField(String name, FieldType fieldType, String fieldValue) {
		FieldValueDto result = new FieldValueDto();
		result.setName(name);
		result.setValue(fieldValue);
		result.setType(fieldType);
		return result;
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
