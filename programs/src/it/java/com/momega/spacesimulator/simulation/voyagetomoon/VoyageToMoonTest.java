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
public class VoyageToMoonTest {

    @Autowired
    private SimulationFactory simulationFactory;
    
    @Autowired
    private Gson gson;
    
    @Autowired
    private VoyageToMoonSimulation simulation;
    
    @Autowired
   	private ThreadPoolTaskExecutor taskExecutor; 
    
    @Test
    public void voyagerTest() throws FileNotFoundException, InterruptedException, ExecutionException {
    	VoyageToMoonFields fields = new VoyageToMoonFields();
    	fields.setStartTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 3, 10, DateTimeZone.UTC)));
    	fields.setEndTime(TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 4, 00, DateTimeZone.UTC)));
    	fields.setStartSpeed(10836);
    	fields.setEndSpeed(10838);
    	fields.setStepInSeconds(10.0);
    	fields.setMinSurface(70 * 1E3);
    	fields.setMaxSurface(200 * 1E3);
    	fields.setMaxEccentricity(1.0173);
    	fields.setSpeedStep(0.1);
    	
    	List<VoyageToMoonResult> results = simulationFactory.runSimulation(simulation, fields, taskExecutor);
    	PrintWriter writer = new PrintWriter(new File("voyageToMoon2.txt"));
    	gson.toJson(results, writer);
    	writer.flush();
    	writer.close();
    }

}
