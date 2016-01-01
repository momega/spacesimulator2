package com.momega.spacesimulator.web.controller;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.momega.spacesimulator.simulation.SimulationDefinition;
import com.momega.spacesimulator.simulation.SimulationHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.momega.spacesimulator.simulation.Simulation;
import com.momega.spacesimulator.simulation.SimulationFactory;

@RestController
@RequestMapping("/simulation")
public class SimulationController {

	private static final Logger logger = LoggerFactory.getLogger(SimulationController.class);
	
	@Autowired
	private SimulationFactory simulationFactory;

	@Autowired
	private SimulationHolder simulationHolder;

	@Autowired
	private SimulationTransformer simulationTransformer;
	
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<SimulationDto> getSimulations() {
   		Collection<Simulation<?,?>> simulations = simulationHolder.getSimulations();
		List<SimulationDto> result = new ArrayList<>();
		for(Simulation<?,?> simulation : simulations) {
			SimulationDefinition simulationDefinition = simulationFactory.findDefinition(simulation.getName());
			result.add(simulationTransformer.transform(simulation, simulationDefinition));
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
	public SimulationDto getSimulation(@PathVariable("uuid") String uuid) {
		Simulation<?, ?> simulation = simulationHolder.findSimulation(uuid);
		SimulationDefinition simulationDefinition = simulationFactory.findDefinition(simulation.getName());
		SimulationDto result = simulationTransformer.transform(simulation, simulationDefinition);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public SimulationDto newDefinition(@RequestBody DefinitionValueDto definitionValueDto) {
		SimulationDefinition simulationDefinition = simulationFactory.findDefinition(definitionValueDto.getName());
		Object parameters = prepareParametersInstance(simulationDefinition, definitionValueDto.getFieldValues());
		Simulation<Object, Object> simulation = (Simulation<Object, Object>) simulationFactory.createSimulation(simulationDefinition.getName());
		simulationHolder.addSimulation(simulation);
		simulation.setParameters(parameters);
		SimulationDto result = simulationTransformer.transform(simulation, simulationDefinition);
		return result;
	}

	protected Object prepareParametersInstance(SimulationDefinition definition, List<FieldValueDto> fieldValues) {
		try {
			Object parametersInstance = definition.getParametersClass().newInstance();
			Map<String, PropertyDescriptor> propertyDescriptorMap = definition.getPropertyDescriptors();
			for(FieldValueDto fieldValue: fieldValues) {
				String fieldName = fieldValue.getName();
				PropertyDescriptor pd = propertyDescriptorMap.get(fieldName);
				Object o = simulationTransformer.getFieldValue(fieldValue);
				pd.getWriteMethod().invoke(parametersInstance, o);
			}
			return parametersInstance;
		} catch (Exception e) {
			throw new IllegalStateException("unable to create parameters instance ", e);
		}
	}

}
