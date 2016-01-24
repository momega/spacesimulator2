package com.momega.spacesimulator.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Scope("prototype")
public abstract class Simulation<F, I> implements Callable<List<I>> {
	
	private final static Logger logger = LoggerFactory.getLogger(Simulation.class);
	
	private final Class<? extends SimulationSolver<F, I>> solverClass;
	private final String uuid;
	private SimulationState simulationState = SimulationState.PREPARING;
	private F fields;
	private List<I> outputs = Collections.synchronizedList(new ArrayList<I>());
	private final String name;
	private Date startedAt = null;
	private Date finishedAt = null;
	private int totalInputs = 0;
	private int completedInputs;
	private int failedInputs;

	private AsyncTaskExecutor taskExecutor;
	
	private List<Future<I>> futures = new ArrayList<>();

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Creates the simulation instance. It is the protected simulation constructor to force simulation
	 * implementations to specify the name, fields and simulation classes.
	 * @param name the name of the simulation
	 * @param callableClass
     */
	protected Simulation(String name, Class<? extends SimulationSolver<F, I>> callableClass) {
		super();
		this.uuid = UUID.randomUUID().toString();
		this.name = name;
		this.solverClass = callableClass;
	}
	
	public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setFields(F fields) {
		this.fields = fields;
	}

	public F getFields() {
		return fields;
	}

	@Override
	public List<I> call() {
		Assert.notNull(getFields());
		logger.info("Simulation {} started", solverClass);
		this.simulationState = SimulationState.RUNNING;
		this.startedAt = new Date();
		this.futures = new ArrayList<>();
		final Predicate<I> testPredicate = createPredicate(getFields());
		List<I> inputs = generateInputs();
		for(I input : inputs) {
			submitInput(getFields(), input);
		}
		this.completedInputs = 0;
		this.failedInputs = 0;
		this.totalInputs = inputs.size();
		
		for(Future<I> f : futures) {
			try {
				I output = f.get();
				if (output!=null && testPredicate.test(output)) {
	            	logger.warn("output = {}", output);
	           		outputs.add(output);
	            }
			} catch (Exception e) {
				failedInputs++;
			}
			this.completedInputs++;
		}
		
		this.finishedAt = new Date();
		logger.info("Simulation {} completed", solverClass);
		simulationState = SimulationState.FINISHED;
		return getOutputs();
	}
	
	public List<I> getOutputs() {
		synchronized (outputs) {
			return Collections.unmodifiableList(outputs);
		}
	}
	
	public void stop() {
		for(Future<I> f : futures) {
			if (!f.isDone()) {
				f.cancel(true);
			}
		}
		futures.clear();
		simulationState = SimulationState.CANCELED;
	}

	protected abstract Predicate<I> createPredicate(F fields);

	protected abstract List<I> generateInputs();

	public void submitInput(F fields, I input) {
		SimulationSolver<F, I> solver = applicationContext.getBean(solverClass);
		solver.setFields(fields);
		solver.setInput(input);
		Future<I> f = taskExecutor.submit(solver);
		futures.add(f);
	}

	public int getTotalInputs() {
		return totalInputs;
	}

	public int getCompletedInputs() {
		return completedInputs;
	}
	
	public int getFailedInputs() {
		return failedInputs;
	}

	public boolean isRunning() {
		return this.simulationState == SimulationState.RUNNING;
	}
	
	public Date getStartedAt() {
		return startedAt;
	}
	
	public Date getFinishedAt() {
		return finishedAt;
	}
	
	public String getName() {
		return name;
	}

	public SimulationState getSimulationState() {
		return simulationState;
	}

	public String getUuid() {
		return uuid;
	}

	@Override
	public String toString() {
		return "Simulation [uuid=" + uuid + ", simulationState="
				+ simulationState + ", name=" + name + "]";
	}

}
