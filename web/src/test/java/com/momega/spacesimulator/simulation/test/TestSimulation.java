package com.momega.spacesimulator.simulation.test;

import com.momega.spacesimulator.simulation.Simulation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
@Scope("prototype")
public class TestSimulation extends Simulation<TestFields, TestResult>{

	public TestSimulation() {
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
		TestFields params = getFields();
		List<TestResult> result = new ArrayList<>();
		for(int i=1;i<=params.getCount();i++) {
			TestResult item = new TestResult();
			item.setNumber(i);
			result.add(item);
		}
		return result;
	}

}
