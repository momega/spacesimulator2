package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.momega.spacesimulator.utils.TimeUtils;

/**
 * Created by martin on 7/19/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class VoyageToMoonTest {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private VoyageToMoonRunnable voyageToMoon;
    
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @Ignore
    public void voyagerTest() {
        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 15, 0, DateTimeZone.UTC));
        Timestamp endTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 13, 0, 0, DateTimeZone.UTC));
        Timestamp t = timestamp;
        List<Future<?>> futures = new ArrayList<>();
        while(t.before(endTime)) {
            for(int speed=10843; speed<10850; speed++) {
            	VoyageToMoonRunnable runnable = applicationContext.getBean(VoyageToMoonRunnable.class);
            	runnable.setTimestamp(t);
            	runnable.setSpeed(speed);
                Future<?> f = taskExecutor.submit(runnable);
                futures.add(f);
            }
            t = t.add(600.0);
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

}
