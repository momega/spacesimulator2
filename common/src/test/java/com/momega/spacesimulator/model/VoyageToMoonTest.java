package com.momega.spacesimulator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.momega.spacesimulator.utils.TimeUtils;

/**
 * Created by martin on 7/19/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SimpleConfig.class})
public class VoyageToMoonTest {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private VoyageToMoon voyageToMoon;

    @Test
    @Ignore
    public void voyagerTest() {
        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 15, 0, DateTimeZone.UTC));
        Timestamp endTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 13, 0, 0, DateTimeZone.UTC));
        Timestamp t = timestamp;
        List<Future<?>> futures = new ArrayList<>();
        while(t.before(endTime)) {
            for(int speed=10843; speed<10850; speed++) {
                Future<?> f = taskExecutor.submit(new VoyageToMoonRunnable(t, (double)speed));
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
    
    @Test
    public void simpleVoyagerTest() throws InterruptedException, ExecutionException {
        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 16, 0, DateTimeZone.UTC));
        double speed = 10844.0;
        
        Future<?> f = taskExecutor.submit(new VoyageToMoonRunnable(timestamp, speed));
        f.get();
    }

    class VoyageToMoonRunnable implements Runnable {

        private final Timestamp timestamp;
        private final double speed;

        VoyageToMoonRunnable(Timestamp timestamp, double speed) {
            this.timestamp = timestamp;
            this.speed = speed;
        }

        @Override
        public void run() {
            voyageToMoon.run(timestamp, speed);
        }
    }

}
