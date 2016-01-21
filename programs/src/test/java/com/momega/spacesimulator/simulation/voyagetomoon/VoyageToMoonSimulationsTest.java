package com.momega.spacesimulator.simulation.voyagetomoon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.momega.spacesimulator.service.utils.TimeUtils;
import com.momega.spacesimulator.simulation.SimulationFactory;
import com.momega.spacesimulator.simulation.TestConfig;

/**
 * Created by martin on 7/19/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class VoyageToMoonSimulationsTest {

    @Autowired
    private SimulationFactory simulationFactory;
    
    @Autowired
    private Gson gson;
    
    @Autowired
    private VoyageToMoonSimulation simulation;
    
    @Autowired
   	private ThreadPoolTaskExecutor taskExecutor; 
    
    @Test
    @Ignore
    public void indicativeVoyagerTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	VoyageToMoonFields parameters = new VoyageToMoonFields();
    	parameters.setStartTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 7, 0, DateTimeZone.UTC)));
    	parameters.setEndTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 18, 0, DateTimeZone.UTC)));
    	parameters.setStartSpeed(10835);
    	parameters.setEndSpeed(10842);
    	parameters.setStepInSeconds(120.0);
    	
    	List<VoyageToMoonResult> results = simulationFactory.runSimulation(simulation, parameters, taskExecutor);
    	PrintWriter writer = new PrintWriter(new File("voyageToMoon.txt"));
    	gson.toJson(results, writer);
    }
    
    @Test
    @Ignore
    public void voyagerTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	VoyageToMoonFields parameters = new VoyageToMoonFields();
    	parameters.setStartTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 11, 30, DateTimeZone.UTC)));
    	parameters.setEndTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 11, 36, DateTimeZone.UTC)));
    	parameters.setStartSpeed(10837);
    	parameters.setEndSpeed(10840);
    	parameters.setStepInSeconds(10.0);
    	
    	List<VoyageToMoonResult> results = simulationFactory.runSimulation(simulation, parameters, taskExecutor);
    	PrintWriter writer = new PrintWriter(new File("voyageToMoon.txt"));
    	gson.toJson(results, writer);
    	writer.flush();
    	writer.close();
    }
    
    @Test
    @Ignore
    public void shortVoyagerTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	VoyageToMoonFields parameters = new VoyageToMoonFields();
    	parameters.setStartTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 15, 50, DateTimeZone.UTC)));
    	parameters.setEndTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 16, 00, DateTimeZone.UTC)));
    	parameters.setStartSpeed(10839);
    	parameters.setEndSpeed(10843);
    	parameters.setStepInSeconds(10.0);
    	
    	List<VoyageToMoonResult> results = simulationFactory.runSimulation(simulation, parameters, taskExecutor);
    	PrintWriter writer = new PrintWriter(new File("voyageToMoon.txt"));
    	gson.toJson(results, writer);
    	writer.flush();
    	writer.close();
    }    

}
