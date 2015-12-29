package com.momega.spacesimulator.web.controller;

import com.momega.spacesimulator.simulation.SimulationDefinition;

import com.momega.spacesimulator.simulation.SimulationsHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by martin on 12/27/15.
 */
@RestController
@RequestMapping(value="/definition")
public class DefinitionController {
	
	private static final Logger logger = LoggerFactory.getLogger(DefinitionController.class);

    @Autowired
    private SimulationsHolder simulationsHolder;

    @Autowired
    private SimulationTransformer simulationTransformer;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<DefinitionDto> getDefinitions() {
        List<SimulationDefinition> defs = simulationsHolder.getDefinitions();
        List<DefinitionDto> result = new ArrayList<>();
        for(SimulationDefinition def : defs) {
        	result.add(simulationTransformer.transform(def));
        }
        logger.info("definitions = {}", result);
        return result;
    }

}
