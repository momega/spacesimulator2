package com.momega.spacesimulator.simulation;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Predicate;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public abstract class Simulation<I> implements Runnable {
	
	private final static Logger logger = LoggerFactory.getLogger(Simulation.class);
	
	private Class<? extends SimulationCallable<I>> simulationClass;
	
	@Autowired
    private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private List<Future<I>> futures = new ArrayList<Future<I>>();
	
	private List<I> outputs = new ArrayList<I>();
	
	private File outputFile;
	
	public final void run() {
		List<I> inputs = generateInputs();
		for(I input : inputs) {
			submitInput(input);
		}
		
		Predicate<I> testPredicate = createPredicate();
		PrintWriter writer = null;
		try {
	        writer = new PrintWriter(new PrintWriter(outputFile, "UTF-8"), true);
			Iterator<Future<I>> i = futures.iterator();
	        while(i.hasNext()) {
	            Future<I> f = i.next();
	            try {
	                I output = f.get();
	                if (output!=null && testPredicate.test(output)) {
	                	logger.warn("simulation output: {}", output);
	                	writer.println(output.toString());
	                	outputs.add(output);
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
		} catch (Exception e) {
			IOUtils.closeQuietly(writer);
		}
	}
	
	protected abstract Predicate<I> createPredicate();

	protected abstract List<I> generateInputs();

	public void submitInput(I input) {
		SimulationCallable<I> callable = applicationContext.getBean(simulationClass);
		callable.setInput(input);
		Future<I> f = taskExecutor.submit(callable);
		futures.add(f);
	}
	
	public void setSimulationClass(Class<? extends SimulationCallable<I>> simulationClass) {
		this.simulationClass = simulationClass;
	}
	
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}
}
