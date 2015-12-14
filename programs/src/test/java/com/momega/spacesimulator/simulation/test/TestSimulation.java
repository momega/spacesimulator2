package com.momega.spacesimulator.simulation.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.simulation.Simulation;

@Component
@Scope("prototype")
public class TestSimulation extends Simulation<TestSimulationParameters, TestSimulationResult>{

	protected TestSimulation(TestSimulationParameters params) {
		super("Test", params, TestSimulationCallable.class);
	}

	@Override
	protected Predicate<TestSimulationResult> createPredicate() {
		return new Predicate<TestSimulationResult>() {
			@Override
			public boolean test(TestSimulationResult t) {
				return true;
			}
		};
	}

	@Override
	protected List<TestSimulationResult> generateInputs() {
		TestSimulationParameters params = getParameters();
		List<TestSimulationResult> result = new ArrayList<>();
		for(int i=1;i<=params.count;i++) {
			TestSimulationResult item = new TestSimulationResult();
			item.setNumber(i);
			result.add(item);
		}
		return result;
	}

}
