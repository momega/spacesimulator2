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
import com.momega.spacesimulator.simulation.moonorbit.MoonOrbitParameters;
import com.momega.spacesimulator.simulation.moonorbit.MoonOrbitResult;
import com.momega.spacesimulator.simulation.moonorbit.MoonOrbitSimulation;

/**
 * Created by martin on 7/19/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class MoonSimulationsTest {

    @Autowired
    private SimulationsHolder simulationsHolder;
    
    @Autowired
    private Gson gson;
    
    @Test
    public void shortMoonOrbitTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	MoonOrbitParameters parameters = new MoonOrbitParameters();
    	parameters.startTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 15, 49, 22, DateTimeZone.UTC));
    	parameters.endTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 15, 49, 23, DateTimeZone.UTC));
    	parameters.speed = 10841.0;
    	parameters.startBurnTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 16, 6, 48, 30, DateTimeZone.UTC));
    	parameters.endBurnTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 16, 6, 48, 31, DateTimeZone.UTC));
    	parameters.stepTime = 1.0;
    	parameters.burnTime = 362.0;
    	
    	Simulation<MoonOrbitParameters, MoonOrbitResult> simulation = simulationsHolder.createSimulation(MoonOrbitSimulation.class, parameters);
		Future<List<MoonOrbitResult>> f = simulationsHolder.getFuture(simulation);
		
		List<MoonOrbitResult> results = f.get();
    	PrintWriter writer = new PrintWriter(new File("moonOrbit.txt"));
    	gson.toJson(results, writer);
    	writer.flush();
    	writer.close();
    }    
    
    @Test
    @Ignore
    public void moonOrbitTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	MoonOrbitParameters parameters = new MoonOrbitParameters();
    	parameters.startTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 15, 40, 0, DateTimeZone.UTC));
    	parameters.endTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 15, 59, 0, DateTimeZone.UTC));
    	parameters.speed = 10841.0;
    	parameters.startBurnTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 16, 6, 45, 0, DateTimeZone.UTC));
    	parameters.endBurnTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 16, 6, 52, 0, DateTimeZone.UTC));
    	parameters.stepTime = 10.0;
    	parameters.burnTime = 362.0;
    	
    	Simulation<MoonOrbitParameters, MoonOrbitResult> simulation = simulationsHolder.createSimulation(MoonOrbitSimulation.class, parameters);
		Future<List<MoonOrbitResult>> f = simulationsHolder.getFuture(simulation);
		
		List<MoonOrbitResult> results = f.get();
    	PrintWriter writer = new PrintWriter(new File("moonOrbit.txt"));
    	gson.toJson(results, writer);
    	writer.flush();
    	writer.close();
    }

}
