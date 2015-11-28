package com.momega.spacesimulator.runnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.momega.spacesimulator.runnable.TestConfig;
import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * Created by martin on 7/19/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class VoyageToMoonTest {
	
	private static final Logger logger = LoggerFactory.getLogger(VoyageToMoonTest.class);

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private VoyageToMoonRunnable voyageToMoon;
    
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @Ignore
    public void voyagerTest() {
        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 9, 0, DateTimeZone.UTC));
        Timestamp endTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 12, 0, DateTimeZone.UTC));
        Timestamp t = timestamp;
        List<Future<?>> futures = new ArrayList<>();
        while(t.before(endTime)) {
            for(int speed=10835; speed<10840; speed++) {
            	VoyageToMoonRunnable runnable = applicationContext.getBean(VoyageToMoonRunnable.class);
            	runnable.setTimestamp(t);
            	runnable.setSpeed(speed);
                Future<?> f = taskExecutor.submit(runnable);
                futures.add(f);
            }
            t = t.add(30.0);
        }

        Iterator<Future<?>> i = futures.iterator();
        while(i.hasNext()) {
            Future<?> f = i.next();
            try {
                f.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Test
    public void indicativeVoyagerTest() {
    	VoyageToMoonResult min = new VoyageToMoonResult();
    	min.distance = Double.MAX_VALUE;
    	
        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 11, 12, 0, DateTimeZone.UTC));
        Timestamp endTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 13, 0, 0, DateTimeZone.UTC));
        Timestamp t = timestamp;
        List<Future<VoyageToMoonResult>> futures = new ArrayList<>();
        while(t.before(endTime)) {
            for(int speed=10833; speed<10850; speed++) {
            	VoyageToMoonRunnable runnable = applicationContext.getBean(VoyageToMoonRunnable.class);
            	runnable.setTimestamp(t);
            	runnable.setSpeed(speed);
                Future<VoyageToMoonResult> f = taskExecutor.submit(runnable);
                futures.add(f);
            }
            t = t.add(300.0);
        }

        Iterator<Future<VoyageToMoonResult>> i = futures.iterator();
        while(i.hasNext()) {
            Future<VoyageToMoonResult> f = i.next();
            try {
                VoyageToMoonResult r = f.get();
                if (r!=null && r.surface < 250E3) {
                	logger.warn("Start at = {}, speed = {}, Dist = {}, velocity = {}, e = {}, timestamp at minimum = {} at {} -> {}", TimeUtils.timeAsString(r.timestamp), r.speed, r.distance, r.velocity, r.keplerianElements.getKeplerianOrbit().getEccentricity(), TimeUtils.timeAsString(r.minTimestamp), r.duration, TimeUtils.durationAsString(r.duration));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
