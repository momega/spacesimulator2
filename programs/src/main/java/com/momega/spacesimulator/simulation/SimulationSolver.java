package com.momega.spacesimulator.simulation;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Martin Vanek
 *
 */
@Component
@Scope("prototype")
public abstract class SimulationSolver<F, I> implements Callable<I>, BiFunction<F, I, I> {
	
	private I input;
	private F fields;
	
	public void setInput(I input) {
		this.input = input;
	}
	
	public I getInput() {
		return input;
	}
	
	public void setFields(F fields) {
		this.fields = fields;
	}
	
	public F getFields() {
		return fields;
	}

	@Override
	public final I call() throws Exception {
		I output = apply(fields, input);
		return output;
	}

}
