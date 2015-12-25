package com.momega.spacesimulator.simulation.voyagetomoon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.momega.spacesimulator.service.utils.TimeUtils;
import com.momega.spacesimulator.simulation.Simulation;
import com.momega.spacesimulator.simulation.SimulationsHolder;
import com.momega.spacesimulator.simulation.TestConfig;

/**
 * Created by martin on 7/19/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class VoyageToMoonSimulationsTest {

    @Autowired
    private SimulationsHolder simulationsHolder;
    
    @Autowired
    private Gson gson;
    
    @Test
    @Ignore
    public void indicativeVoyagerTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	VoyageToMoonParameters parameters = new VoyageToMoonParameters();
    	parameters.startTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 7, 0, DateTimeZone.UTC));
    	parameters.endTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 18, 0, DateTimeZone.UTC));
    	parameters.startSpeed = 10835;
    	parameters.endSpeed = 10842;
    	parameters.stepInSeconds = 120.0;
    	
    	Simulation<VoyageToMoonParameters, VoyageToMoonResult> simulation = simulationsHolder.createSimulation(VoyageToMoonSimulation.class, parameters);
		Future<List<VoyageToMoonResult>> f = simulationsHolder.getFuture(simulation);
		List<VoyageToMoonResult> results = f.get();
    	PrintWriter writer = new PrintWriter(new File("voyageToMoon.txt"));
    	gson.toJson(results, writer);
    }
    
    @Test
    @Ignore
    public void voyagerTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	VoyageToMoonParameters parameters = new VoyageToMoonParameters();
    	parameters.startTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 11, 30, DateTimeZone.UTC));
    	parameters.endTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 11, 36, DateTimeZone.UTC));
    	parameters.startSpeed = 10837;
    	parameters.endSpeed = 10840;
    	parameters.stepInSeconds = 10.0;
    	
    	Simulation<VoyageToMoonParameters, VoyageToMoonResult> simulation = simulationsHolder.createSimulation(VoyageToMoonSimulation.class, parameters);
		Future<List<VoyageToMoonResult>> f = simulationsHolder.getFuture(simulation);
		List<VoyageToMoonResult> results = f.get();
    	PrintWriter writer = new PrintWriter(new File("voyageToMoon.txt"));
    	gson.toJson(results, writer);
    	writer.flush();
    	writer.close();
    }
    
    @Test
    @Ignore
    public void shortVoyagerTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	VoyageToMoonParameters parameters = new VoyageToMoonParameters();
    	parameters.startTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 15, 50, DateTimeZone.UTC));
    	parameters.endTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 16, 00, DateTimeZone.UTC));
    	parameters.startSpeed = 10839;
    	parameters.endSpeed = 10843;
    	parameters.stepInSeconds = 10.0;
    	
    	Simulation<VoyageToMoonParameters, VoyageToMoonResult> simulation = simulationsHolder.createSimulation(VoyageToMoonSimulation.class, parameters);
		Future<List<VoyageToMoonResult>> f = simulationsHolder.getFuture(simulation);
		List<VoyageToMoonResult> results = f.get();
    	PrintWriter writer = new PrintWriter(new File("voyageToMoon.txt"));
    	gson.toJson(results, writer);
    	writer.flush();
    	writer.close();
    }    

}