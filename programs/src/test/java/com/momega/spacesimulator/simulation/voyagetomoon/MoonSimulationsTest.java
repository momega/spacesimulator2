package com.momega.spacesimulator.simulation.voyagetomoon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
    public void moonOrbitTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	MoonOrbitParameters parameters = new MoonOrbitParameters();
    	parameters.timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 15, 39, 00, DateTimeZone.UTC));
    	parameters.speed = 10841.0;
    	parameters.minTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 16, 6, 58, 33, DateTimeZone.UTC));
    	
    	parameters.minBurnTime = 360;
    	parameters.maxBurnTime = parameters.minBurnTime+10;
    	parameters.burnTimeStep = 10;
    	
//    	parameters.timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 11, 31, 50, DateTimeZone.UTC));
//    	parameters.speed = 10838.0;
//    	parameters.minTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 16, 8, 40, 22, DateTimeZone.UTC));
//    	parameters.minBurnTime = 1000;
//    	parameters.maxBurnTime = 2400;
//    	parameters.burnTimeStep = 10;    	
    	
    	Simulation<MoonOrbitParameters, MoonOrbitResult> simulation = simulationsHolder.createSimulation(MoonOrbitSimulation.class, parameters);
		Future<List<MoonOrbitResult>> f = simulationsHolder.getFuture(simulation);
		List<MoonOrbitResult> results = f.get();
//    	PrintWriter writer = new PrintWriter(new File("moonOrbit.txt"));
//    	gson.toJson(results, writer);
//    	writer.flush();
//    	writer.close();
    }

}
