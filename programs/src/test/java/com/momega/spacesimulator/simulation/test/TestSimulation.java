package com.momega.spacesimulator.simulation.test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.simulation.Simulation;

@Component
@Scope("prototype")
public class TestSimulation extends Simulation<TestParameters, TestResult>{

	protected TestSimulation() {
		super("Test", TestCallable.class);
	}

	@Override
	protected Predicate<TestResult> createPredicate() {
		return new Predicate<TestResult>() {
			@Override
			public boolean test(TestResult t) {
				return true;
			}
		};
	}

	@Override
	protected List<TestResult> generateInputs() {
		TestParameters params = getParameters();
		List<TestResult> result = new ArrayList<>();
		for(int i=1;i<=params.count;i++) {
			TestResult item = new TestResult();
			item.setNumber(i);
			result.add(item);
		}
		return result;
	}

}
