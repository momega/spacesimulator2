package com.momega.spacesimulator.simulation;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public abstract class Simulation<P, I> implements Callable<List<I>>, Function<P, List<I>> {
	
	private final static Logger logger = LoggerFactory.getLogger(Simulation.class);
	
	private final Class<? extends SimulationCallable<I>> callableClass;
	private final String uuid;
	private SimulationState simulationState = SimulationState.PREPARING;
	private P parameters;
	private List<Future<I>> futures = new ArrayList<Future<I>>();
	private List<I> outputs = new ArrayList<I>();
	private final String name;
	private Date startedAt = null;
	private Date finishedAt = null;
	private int totalInputs = 0;
	private int completedInputs = 0;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Creates the simulation instance. It is the protected simulation constructor to force simulation
	 * implementations to specify the name, parameters and simulation classes.
	 * @param name the name of the simulation
	 * @param callableClass
     */
	protected Simulation(String name, Class<? extends SimulationCallable<I>> callableClass) {
		super();
		this.uuid = UUID.randomUUID().toString();
		this.name = name;
		this.callableClass = callableClass;
	}

	public void setParameters(P parameters) {
		this.parameters = parameters;
	}

	public P getParameters() {
		return parameters;
	}

	public final List<I> call() {
		return apply(getParameters());
	}
	
	public final List<I> apply(P parameters) {
		logger.info("Simulation {} started", callableClass);
		this.simulationState = SimulationState.RUNNING;
		this.startedAt = new Date();
		List<I> inputs = generateInputs();
		for(I input : inputs) {
			submitInput(input);
		}
		this.totalInputs = inputs.size();
		this.completedInputs = 0;
		
		Predicate<I> testPredicate = createPredicate();
		Iterator<Future<I>> i = futures.iterator();
        while(i.hasNext()) {
            Future<I> f = i.next();
            try {
                I output = f.get();
                if (output!=null && testPredicate.test(output)) {
                	logger.warn("output = {}", output);
                	outputs.add(output);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            completedInputs++;
            logger.info("{}/{}", completedInputs, totalInputs);
        }
		this.finishedAt = new Date();
		logger.info("Simulation {} completed", callableClass);
		simulationState = SimulationState.FINISHED;
		return getOutputs();
	}
	
	public List<I> getOutputs() {
		return Collections.unmodifiableList(outputs);
	}

	protected abstract Predicate<I> createPredicate();

	protected abstract List<I> generateInputs();

	public void submitInput(I input) {
		SimulationCallable<I> callable = applicationContext.getBean(callableClass);
		callable.setInput(input);
		Future<I> f = taskExecutor.submit(callable);
		futures.add(f);
	}

	public int getTotalInputs() {
		return totalInputs;
	}

	public int getCompletedInputs() {
		return completedInputs;
	}

	public boolean isRunning() {
		return (this.startedAt != null);
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
		return "Simulation{" +
				"uuid='" + uuid + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
