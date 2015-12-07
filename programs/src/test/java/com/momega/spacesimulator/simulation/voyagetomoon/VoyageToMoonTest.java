package com.momega.spacesimulator.simulation.voyagetomoon;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by martin on 7/19/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {VoyageToMoonConfig.class})
public class VoyageToMoonTest {

    @Autowired
    private VoyageToMoonSimulation voyageToMoonSimulation;
    
    @Test
    @Ignore
    public void indicativeVoyagerTest() {
    	voyageToMoonSimulation.run();
    }

//    @Test
//    @Ignore
//    public void voyagerTest() {
//        Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 9, 0, DateTimeZone.UTC));
//        Timestamp endTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 12, 0, DateTimeZone.UTC));
//        Timestamp t = timestamp;
//        List<Future<?>> futures = new ArrayList<>();
//        while(t.before(endTime)) {
//            for(int speed=10835; speed<10840; speed++) {
//            	VoyageToMoonRunnable runnable = applicationContext.getBean(VoyageToMoonRunnable.class);
//            	runnable.setTimestamp(t);
//            	runnable.setSpeed(speed);
//                Future<?> f = taskExecutor.submit(runnable);
//                futures.add(f);
//            }
//            t = t.add(30.0);
//        }
//
//        Iterator<Future<?>> i = futures.iterator();
//        while(i.hasNext()) {
//            Future<?> f = i.next();
//            try {
//                f.get();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    

}
