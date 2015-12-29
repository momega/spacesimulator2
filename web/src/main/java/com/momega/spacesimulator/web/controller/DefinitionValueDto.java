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
public class DefinitionValueDto {

	private String name;
	private List<FieldValueDto> fields = new ArrayList<>();
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setFields(List<FieldValueDto> parameters) {
		this.fields = parameters;
	}
	
	public List<FieldValueDto> getFields() {
		return fields;
	}

	@Override
	public String toString() {
		return "DefinitionDto [name=" + name + ", fields=" + fields
				+ "]";
	}

	
}
