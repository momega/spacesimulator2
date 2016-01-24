package com.momega.spacesimulator.simulation.moonorbit;

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
import com.momega.spacesimulator.simulation.moonorbit.MoonOrbitFields;
import com.momega.spacesimulator.simulation.moonorbit.MoonOrbitResult;
import com.momega.spacesimulator.simulation.moonorbit.MoonOrbitSimulation;

/**
 * Created by martin on 7/19/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class MoonSimulationsTest {

    @Autowired
    private SimulationFactory simulationFactory;
    
    @Autowired
    private Gson gson;
    
    @Autowired
    private MoonOrbitSimulation simulation;
    
    @Autowired
	private ThreadPoolTaskExecutor taskExecutor; 
    
    @Test
    @Ignore
    public void shortMoonOrbitTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	MoonOrbitFields fields = new MoonOrbitFields();
    	fields.setStartTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 15, 49, 22, DateTimeZone.UTC)));
    	fields.setEndTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 15, 49, 23, DateTimeZone.UTC)));
    	fields.setSpeed(10836.9);
    	fields.setStartBurnTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 16, 6, 48, 30, DateTimeZone.UTC)));
    	fields.setEndBurnTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 16, 6, 48, 31, DateTimeZone.UTC)));
    	fields.setStepTime(1.0);
    	fields.setBurnTime(362.0);
    	
    	fields.setApolune(2000 * 1E3);
    	fields.setPerilune(50 * 1E3);
    	fields.setMaxEccentricity(0.4);
    	
    	
    	List<MoonOrbitResult> results = simulationFactory.runSimulation(simulation, fields, taskExecutor);
    	
    	PrintWriter writer = new PrintWriter(new File("moonOrbit.txt"));
    	gson.toJson(results, writer);
    	writer.flush();
    	writer.close();
    }    
    
    @Test
    public void moonOrbitTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	MoonOrbitFields fields = new MoonOrbitFields();
    	fields.setStartTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 3, 10, 0, DateTimeZone.UTC)));
    	fields.setEndTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 3, 20, 0, DateTimeZone.UTC)));
    	fields.setSpeed(10836.9);
    	fields.setStartBurnTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 16, 6, 40, 0, DateTimeZone.UTC)));
    	fields.setEndBurnTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 16, 8, 50, 0, DateTimeZone.UTC)));
    	fields.setStepTime(10.0);
    	fields.setBurnTime(362.0);
    	
    	List<MoonOrbitResult> results = simulationFactory.runSimulation(simulation, fields, taskExecutor);
    	
    	PrintWriter writer = new PrintWriter(new File("moonOrbit.txt"));
    	gson.toJson(results, writer);
    	writer.flush();
    	writer.close();
    }

}
