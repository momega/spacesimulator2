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
	private List<FieldDto> fields = new ArrayList<>();
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setFields(List<FieldDto> parameters) {
		this.fields = parameters;
	}
	
	public List<FieldDto> getFields() {
		return fields;
	}

	@Override
	public String toString() {
		return "DefinitionDto [name=" + name + ", fields=" + fields
				+ "]";
	}

	
}
