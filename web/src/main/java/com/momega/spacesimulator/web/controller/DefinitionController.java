package com.momega.spacesimulator.web.controller;

import com.momega.spacesimulator.simulation.SimulationDefinition;
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
@RequestMapping("/definition")
public class DefinitionController {

    @Autowired
    private ApplicationContext applicationContext;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<SimulationDefinition> getDefinitions() {
        Map<String, SimulationDefinition> defs = applicationContext.getBeansOfType(SimulationDefinition.class);
        return new ArrayList<>(defs.values());
    }
}
