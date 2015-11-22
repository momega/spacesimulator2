/**
 * 
 */
package com.momega.spacesimulator.model;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.momega.spacesimulator.utils.TimeUtils;

/**
 * @author martin
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class RunnableTest {
	
	@Autowired
    private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
    private VoyageToMoonRunnable voyageToMoonRunnable;
	
	@Autowired
    private MoonOrbitRunnable moonOrbitRunnable;

    @Test
    public void earthToMoonTest() throws InterruptedException, ExecutionException {
        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 16, 0, DateTimeZone.UTC));
        double speed = 10844.0;
        
    	voyageToMoonRunnable.setTimestamp(timestamp);
    	voyageToMoonRunnable.setSpeed(speed);
        Future<?> f = taskExecutor.submit(voyageToMoonRunnable);
        f.get();
    }
    
    @Test
    public void moonOrbitTest() throws InterruptedException, ExecutionException {
        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 16, 0, DateTimeZone.UTC));
        double speed = 10844.0;
        
        moonOrbitRunnable.setTimestamp(timestamp);
        moonOrbitRunnable.setSpeed(speed);
        Future<?> f = taskExecutor.submit(moonOrbitRunnable);
        f.get();
    }
}
