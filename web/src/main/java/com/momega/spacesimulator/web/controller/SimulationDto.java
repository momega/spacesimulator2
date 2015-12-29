package com.momega.spacesimulator.web.controller;

import com.momega.spacesimulator.simulation.SimulationState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by martin on 12/26/15.
 */
public class SimulationDto {

    private String name;
    private SimulationState simulationState;
    private Date startedAt = null;
    private Date finishedAt = null;
    private int totalInputs;
    private int completedInputs;
    private String uuid;
    private List<FieldValueDto> fieldValues = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SimulationState getSimulationState() {
        return simulationState;
    }

    public void setSimulationState(SimulationState simulationState) {
        this.simulationState = simulationState;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public int getTotalInputs() {
        return totalInputs;
    }

    public void setTotalInputs(int totalInputs) {
        this.totalInputs = totalInputs;
    }

    public int getCompletedInputs() {
        return completedInputs;
    }

    public void setCompletedInputs(int completedInputs) {
        this.completedInputs = completedInputs;
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
				+ ", simulationState=" + simulationState + ", startedAt="
				+ startedAt + ", finishedAt=" + finishedAt + ", totalInputs="
				+ totalInputs + ", completedInputs=" + completedInputs
				+ ", fieldValues=" + fieldValues + "]";
	}
    
}
