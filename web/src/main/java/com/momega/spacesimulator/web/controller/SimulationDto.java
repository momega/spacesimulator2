package com.momega.spacesimulator.web.controller;

import java.util.Date;

import com.momega.spacesimulator.simulation.SimulationState;

/**
 * Created by martin on 12/26/15.
 */
public class SimulationDto extends BasicSimulationDto {

    private SimulationState simulationState;
    private Date startedAt = null;
    private Date finishedAt = null;
    private int totalInputs;
    private int completedInputs;

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

	@Override
	public String toString() {
		return "SimulationDto [simulationState=" + simulationState
				+ ", startedAt=" + startedAt + ", finishedAt=" + finishedAt
				+ ", totalInputs=" + totalInputs + ", completedInputs="
				+ completedInputs + ", getName()=" + getName() + ", getUuid()="
				+ getUuid() + ", getFieldValues()=" + getFieldValues() + "]";
	}

    
}
