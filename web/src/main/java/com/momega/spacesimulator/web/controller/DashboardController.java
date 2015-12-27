package com.momega.spacesimulator.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.momega.spacesimulator.simulation.SimulationsHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.momega.spacesimulator.simulation.Simulation;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
	
	@Autowired
	private SimulationsHolder simulationsHolder;
	
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<SimulationDto> getSimulations() {
   		Collection<Simulation<?,?>> simulations = simulationsHolder.getSimulations();
		List<SimulationDto> result = new ArrayList<>();
		for(Simulation<?,?> simulation : simulations) {
			result.add(transform(simulation));
		}
		return result;
	}

	protected SimulationDto transform(Simulation<?,?> simulation) {
		SimulationDto dto = new SimulationDto();
		dto.setName(simulation.getName());
		dto.setCompletedInputs(simulation.getCompletedInputs());
		dto.setFinishedAt(simulation.getFinishedAt());
		dto.setStartedAt(simulation.getStartedAt());
		dto.setSimulationState(simulation.getSimulationState());
		dto.setUuid(simulation.getUuid());
		return dto;
	}
}
