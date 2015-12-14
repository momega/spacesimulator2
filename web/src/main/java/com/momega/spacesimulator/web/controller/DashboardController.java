package com.momega.spacesimulator.web.controller;

import java.util.Map;

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
	private ApplicationContext applicationContext;
	
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String[] getSimulations() {
		String[] beanNames = applicationContext.getParent().getBeanNamesForType(Simulation.class);
		return beanNames;
	}
}
