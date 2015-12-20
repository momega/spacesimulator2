package com.momega.spacesimulator.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
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
public abstract class Simulation<P, I> implements Callable<List<I>> {
	
	private final static Logger logger = LoggerFactory.getLogger(Simulation.class);
	
	private final Class<? extends SimulationCallable<I>> simulationClass;

	private P parameters;
	
	protected Simulation(String name, P parameters, Class<? extends SimulationCallable<I>> simulationClass) {
		super();
		this.name = name;
		this.parameters = parameters;
		this.simulationClass = simulationClass;
	}

	@Autowired
    private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private List<Future<I>> futures = new ArrayList<Future<I>>();
	
	private List<I> outputs = new ArrayList<I>();
	
	private String name;
	private Date startedAt = null;
	private Date finishedAt = null;
	private int totalInputs = 0;
	private int completedInputs = 0;
	
	public final List<I> call() {
		logger.info("Simulation {} started", simulationClass);
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
                	logger.info("output = {}", output);
                	outputs.add(output);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            completedInputs++;
        }
		this.finishedAt = new Date();
		logger.info("Simulation {} completed", simulationClass);
		return getOutputs();
	}
	
	public List<I> getOutputs() {
		return Collections.unmodifiableList(outputs);
	}
	
	public int getTotalInputs() {
		return totalInputs;
	}
	
	public int getCompletedInputs() {
		return completedInputs;
	}
	
	protected abstract Predicate<I> createPredicate();

	protected abstract List<I> generateInputs();

	public void submitInput(I input) {
		SimulationCallable<I> callable = applicationContext.getBean(simulationClass);
		callable.setInput(input);
		Future<I> f = taskExecutor.submit(callable);
		futures.add(f);
	}
	
	public P getParameters() {
		return parameters;
	}
	
	public boolean isRunning() {
		return (this.startedAt != null);
	}
	
	public Date getStarted() {
		return startedAt;
	}
	
	public Date getFinished() {
		return finishedAt;
	}
	
	public String getName() {
		return name;
	}
	
}
