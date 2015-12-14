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
public class VoyageToMoonTest {

    @Autowired
    private SimulationsHolder simulationsHolder;
    
    @Autowired
    private Gson gson;
    
    @Test
    @Ignore
    public void indicativeVoyagerTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	VoyageToMoonParameters parameters = new VoyageToMoonParameters();
    	parameters.startTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 6, 0, DateTimeZone.UTC));
    	parameters.endTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 20, 0, DateTimeZone.UTC));
    	parameters.startSpeed = 10834;
    	parameters.endSpeed = 10843;
    	parameters.stepInSeconds = 180.0;
    	
    	Simulation<VoyageToMoonParameters, VoyageToMoonResult> simulation = simulationsHolder.createSimulation(VoyageToMoonSimulation.class, parameters);
		Future<List<VoyageToMoonResult>> f = simulationsHolder.getFuture(simulation);
		List<VoyageToMoonResult> results = f.get();
    	PrintWriter writer = new PrintWriter(new File("voyageToMoon.txt"));
    	gson.toJson(results, writer);
    }
    
    @Test
    public void voyagerTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	VoyageToMoonParameters parameters = new VoyageToMoonParameters();
    	parameters.startTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 11, 27, DateTimeZone.UTC));
    	parameters.endTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 11, 28, DateTimeZone.UTC));
    	parameters.startSpeed = 10838;
    	parameters.endSpeed = 10839;
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
