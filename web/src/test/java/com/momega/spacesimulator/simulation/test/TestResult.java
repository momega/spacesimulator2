package com.momega.spacesimulator.simulation.test;

public class TestResult {

	private int number = 0;
	private boolean finished = false;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	@Override
	public String toString() {
		return "TestSimulationResult [number=" + number + ", finished="
				+ finished + "]";
	}
	
}
