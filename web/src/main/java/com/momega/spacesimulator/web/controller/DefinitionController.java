package com.momega.spacesimulator.web.controller;

import com.momega.spacesimulator.simulation.SimulationDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    private ApplicationContext applicationContext;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<DefinitionDto> getDefinitions() {
        Map<String, SimulationDefinition> defs = applicationContext.getBeansOfType(SimulationDefinition.class);
        List<DefinitionDto> result = new ArrayList<>();
        for(SimulationDefinition def : defs.values()) {
        	result.add(transform(def));
        }
        logger.info("definitions = {}", result);
        return result;
    }
    
    protected DefinitionDto transform(SimulationDefinition def) {
    	DefinitionDto dto = new DefinitionDto();
    	dto.setName(def.getName());
    	for(Map.Entry<String, String> entry : def.getParametersDefinition().entrySet()) {
    		ParameterDto parameterDto = new ParameterDto();
    		parameterDto.setName(entry.getKey());
    		parameterDto.setType(entry.getValue());
    		dto.getParameters().add(parameterDto);
    	}
    	return dto;
    }
}
