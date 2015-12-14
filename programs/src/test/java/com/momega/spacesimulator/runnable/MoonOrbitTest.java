/**
 * 
 */
package com.momega.spacesimulator.runnable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


//import com.momega.spacesimulator.dynamic.InstantManager;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.service.ModelService;
import com.momega.spacesimulator.service.utils.TimeUtils;
import com.momega.spacesimulator.simulation.TestConfig;
import com.momega.spacesimulator.simulation.voyagetomoon.VoyageToMoonCallable;

/**
 * @author martin
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class MoonOrbitTest {
	
	private static final Logger logger = LoggerFactory.getLogger(MoonOrbitTest.class);
	
	@Autowired
    private ThreadPoolTaskExecutor taskExecutor;
	
//	@Autowired
//    private VoyageToMoonRunnable voyageToMoonRunnable;
	
	@Autowired
    private MoonOrbitRunnable moonOrbitRunnable;
	
//	@Autowired
//    private InstantManager instantManager;
	
	@Autowired
    private ModelService modelService;

    @Test
    @Ignore
    public void earthToMoonTest() throws InterruptedException, ExecutionException {
//        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 16, 0, DateTimeZone.UTC));
//        double speed = 10844.0;
//        
//    	voyageToMoonRunnable.setTimestamp(timestamp);
//    	voyageToMoonRunnable.setSpeed(speed);
//        Future<?> f = taskExecutor.submit(voyageToMoonRunnable);
//        f.get();
    }
//    
//    @Test
//    @Ignore
//    public void moonOrbitTest() throws InterruptedException, ExecutionException {
//        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 11, 29, 30, DateTimeZone.UTC));
//        double speed = 10838.0;
//        
//        MoonOrbitResult min = new MoonOrbitResult();
//        min.period = Double.MAX_VALUE;
//        min.eccentricity = Double.MAX_VALUE;
//        
//        for(long startBurnTime=333590; startBurnTime<=333760; startBurnTime= startBurnTime+5) {
//        	long burnTime = 2700;
//        	//for(long burnTime=2600; burnTime<2800; burnTime=burnTime+5) {
//	        	logger.info("start burn time {} with {}", startBurnTime, burnTime);
//		        moonOrbitRunnable.setTimestamp(timestamp);
//		        moonOrbitRunnable.setSpeed(speed);
//		        moonOrbitRunnable.setStartBurnTime(startBurnTime);
//		        moonOrbitRunnable.setBurnTime(burnTime);
//		        Future<MoonOrbitResult> f = taskExecutor.submit(moonOrbitRunnable);
//		        MoonOrbitResult m = f.get();
//		        
//		        if (m.eccentricity<0.2 && m.perilune>90E3 && m.perilune<120E3) {
//		        	logger.warn("orbit at end of the burn = {}, end vel = {}, perilune {}km, apolune {}km, start burn = {}, burn time = {}", m.keplerianElements, m.velocity, m.perilune / 1000, m.apolune / 1000, startBurnTime, burnTime);
//		        	if (min.eccentricity>m.eccentricity) {
//			        	min=m;
//		        	}
//		        }
//        	//}
//    	}
//        
//        logger.warn("final result = {}, {} with period {} up to surface", min.burnTime, min.startBurnTime, min.period, min.perilune);
//    }
}
