package com.momega.spacesimulator.web.controller;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.momega.spacesimulator.simulation.SimulationDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.momega.spacesimulator.simulation.Simulation;
import com.momega.spacesimulator.simulation.SimulationsHolder;

@RestController
@RequestMapping("/simulation")
public class SimulationController {

	private static final Logger logger = LoggerFactory.getLogger(SimulationController.class);
	
	@Autowired
	private SimulationsHolder simulationsHolder;

	@Autowired
	private SimulationTransformer simulationTransformer;
	
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<SimulationDto> getSimulations() {
   		Collection<Simulation<?,?>> simulations = simulationsHolder.getSimulations();
		List<SimulationDto> result = new ArrayList<>();
		for(Simulation<?,?> simulation : simulations) {
			SimulationDefinition simulationDefinition = simulationsHolder.findDefinition(simulation.getName());
			result.add(transform(simulation, simulationDefinition));
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public SimulationDto newDefinition(@RequestBody DefinitionValueDto definitionValueDto) {
		SimulationDefinition simulationDefinition = simulationsHolder.findDefinition(definitionValueDto.getName());
		Object parameters = prepareParametersInstance(simulationDefinition, definitionValueDto.getFields());
		Simulation<Object, Object> simulation = (Simulation<Object, Object>) simulationsHolder.createSimulation(simulationDefinition.getName());
		simulation.setParameters(parameters);
		return transform(simulation, simulationDefinition);
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

	protected SimulationDto transform(Simulation<?, ?> simulation, SimulationDefinition simulationDefinition) {
		SimulationDto dto = new SimulationDto();
		dto.setName(simulation.getName());
		dto.setCompletedInputs(simulation.getCompletedInputs());
		dto.setFinishedAt(simulation.getFinishedAt());
		dto.setStartedAt(simulation.getStartedAt());
		dto.setSimulationState(simulation.getSimulationState());
		dto.setUuid(simulation.getUuid());
		Map<String, PropertyDescriptor> propertyDescriptorMap = simulationDefinition.getPropertyDescriptors();
		for(Map.Entry<String, PropertyDescriptor> entry : propertyDescriptorMap.entrySet()) {
			FieldValueDto fieldValueDto = simulationTransformer.getFieldValue(simulation.getParameters(), entry.getValue());
			dto.getFieldValues().add(fieldValueDto);
		}
		return dto;
	}
}
