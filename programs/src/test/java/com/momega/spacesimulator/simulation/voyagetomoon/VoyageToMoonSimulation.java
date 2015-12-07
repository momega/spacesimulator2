/**
 * 
 */
package com.momega.spacesimulator.simulation.voyagetomoon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.simulation.Simulation;
import com.momega.spacesimulator.utils.TimeUtils;

/**
 * @author Martin Vanek
 *
 */
@Component
public class VoyageToMoonSimulation extends Simulation<VoyageToMoonResult> {
	
	public VoyageToMoonSimulation() {
		super();
		setSimulationClass(VoyageToMoonCallable.class);
		setOutputFile(new File("C:/git/spacesimulator2/programs/voyageToMoon.txt"));
	}

	@Override
	protected List<VoyageToMoonResult> generateInputs() {
		Timestamp timestamp = TimeUtils.fromDateTime(new DateTime(2014, 9, 11, 17, 0, DateTimeZone.UTC));
        Timestamp endTime = TimeUtils.fromDateTime(new DateTime(2014, 9, 12, 20, 0, DateTimeZone.UTC));
        Timestamp t = timestamp;
        List<VoyageToMoonResult> result = new ArrayList<>();
        while(t.before(endTime)) {
            for(int speed=10834; speed<10843; speed++) {
            	VoyageToMoonResult input = new VoyageToMoonResult();
            	input.timestamp = t;
            	input.speed = speed;
            	result.add(input);
            }
            t = t.add(60.0);
        }
        return result;
	}
	
	@Override
	protected Predicate<VoyageToMoonResult> createPredicate() {
		return new Predicate<VoyageToMoonResult>() {
			@Override
			public boolean test(VoyageToMoonResult output) {
				return output.surface>80E3 && output.surface<200E3;
			}
		};
	}

}
