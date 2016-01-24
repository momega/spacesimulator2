package com.momega.spacesimulator.simulation.voyagetomoon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
public class IndicativeTest {

    @Autowired
    private SimulationFactory simulationFactory;
    
    @Autowired
    private Gson gson;
    
    @Autowired
    private VoyageToMoonSimulation simulation;
    
    @Autowired
   	private ThreadPoolTaskExecutor taskExecutor; 
    
    @Test
    public void indicativeVoyagerTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	VoyageToMoonFields fields = new VoyageToMoonFields();
    	fields.setStartTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 2, 0, DateTimeZone.UTC)));
    	fields.setEndTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 10, 0, DateTimeZone.UTC)));
    	fields.setStartSpeed(10835.0);
    	fields.setEndSpeed(10842.0);
    	fields.setStepInSeconds(60.0);
    	fields.setMinSurface(70* 1E3);
    	fields.setMaxSurface(500* 1E3);
    	fields.setMaxEccentricity(1.02);
    	fields.setSpeedStep(1);
    	
    	List<VoyageToMoonResult> results = simulationFactory.runSimulation(simulation, fields, taskExecutor);
    	PrintWriter writer = new PrintWriter(new File("voyageToMoon.txt"));
    	gson.toJson(results, writer);
    }

}
