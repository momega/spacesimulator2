package com.momega.spacesimulator.web.controller;

public class FieldDto {

	private String name;
	private FieldType type;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FieldType getType() {
		return type;
	}
	public void setType(FieldType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "ParameterDto [name=" + name + ", type=" + type + "]";
	}

}
