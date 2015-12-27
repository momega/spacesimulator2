/**
 * 
 */
package com.momega.spacesimulator.web.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * @author martin
 *
 */
public class DefinitionDto {

	private String name;
	private List<ParameterDto> parameters = new ArrayList<>();
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setParameters(List<ParameterDto> parameters) {
		this.parameters = parameters;
	}
	
	public List<ParameterDto> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		return "DefinitionDto [name=" + name + ", parameters=" + parameters
				+ "]";
	}

	
}
