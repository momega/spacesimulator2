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
	private List<FieldValueDto> fieldValues = new ArrayList<>();
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setFieldValues(List<FieldValueDto> parameters) {
		this.fieldValues = parameters;
	}
	
	public List<FieldValueDto> getFieldValues() {
		return fieldValues;
	}

	@Override
	public String toString() {
		return "DefinitionDto [name=" + name + ", fieldValues=" + fieldValues
				+ "]";
	}

	
}
