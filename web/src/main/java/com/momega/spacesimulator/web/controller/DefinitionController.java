package com.momega.spacesimulator.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.momega.spacesimulator.simulation.SimulationDefinition;
import com.momega.spacesimulator.web.service.DefinitionService;

/**
 * Created by martin on 12/27/15.
 */
@RestController
@RequestMapping(value="/api/definition")
public class DefinitionController {
	
	private static final Logger logger = LoggerFactory.getLogger(DefinitionController.class);

    @Autowired
    private DefinitionService definitionService;

    @Autowired
    private SimulationTransformer simulationTransformer;

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<DefinitionDto> getDefinitions() {
        List<SimulationDefinition> defs = definitionService.getDefinitions();
        List<DefinitionDto> result = new ArrayList<>();
        for(SimulationDefinition def : defs) {
        	result.add(simulationTransformer.transform(def));
        }
        Collections.sort(result, new Comparator<DefinitionDto>() {
			@Override
			public int compare(DefinitionDto d1, DefinitionDto d2) {
				return d1.getName().compareTo(d2.getName());
			}
		});
        logger.info("definitions = {}", result);
        return result;
    }

}
