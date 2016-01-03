package com.momega.spacesimulator.web.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 12/26/15.
 */
public class BasicSimulationDto {

    private String name;
    private String uuid;
    private List<FieldValueDto> fieldValues = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
    
    public List<FieldValueDto> getFieldValues() {
		return fieldValues;
	}
    
    public void setFieldValues(List<FieldValueDto> fieldValues) {
		this.fieldValues = fieldValues;
	}

	@Override
	public String toString() {
		return "SimulationDto [uuid=" + uuid + ", name=" + name
				+ ", fieldValues=" + fieldValues + "]";
	}
    
}
