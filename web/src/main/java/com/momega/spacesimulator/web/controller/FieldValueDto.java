/**
 * 
 */
package com.momega.spacesimulator.web.controller;

/**
 * @author martin
 *
 */
public class FieldValueDto {

	private String name;
	private FieldType type;
	private String value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "FieldValueDto{" +
				"name='" + name + '\'' +
				", type='" + type + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
