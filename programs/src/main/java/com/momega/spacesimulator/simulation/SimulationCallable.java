package com.momega.spacesimulator.simulation;

import java.util.concurrent.Callable;
import java.util.function.Function;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Martin Vanek
 *
 */
@Component
@Scope("prototype")
public abstract class SimulationCallable<I> implements Callable<I>, Function<I, I> {
	
	private I input;
	
	public void setInput(I input) {
		this.input = input;
	}
	
	public I getInput() {
		return input;
	}

	@Override
	public I call() throws Exception {
		return apply(input);
	}

}
