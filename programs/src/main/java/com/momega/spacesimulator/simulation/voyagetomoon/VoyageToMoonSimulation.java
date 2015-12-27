/**
 * 
 */
package com.momega.spacesimulator.simulation.voyagetomoon;

import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.simulation.Simulation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Martin Vanek
 *
 */
@Component
@Scope("prototype")
public class VoyageToMoonSimulation extends Simulation<VoyageToMoonParameters, VoyageToMoonResult> {
	
	public VoyageToMoonSimulation() {
		super("Voyage To Moon", VoyageToMoonCallable.class);
	}

	@Override
	protected List<VoyageToMoonResult> generateInputs() {
		VoyageToMoonParameters parameters = getParameters();
		Timestamp timestamp = parameters.startTime;
        Timestamp endTime = parameters.endTime;
        Timestamp t = timestamp;
        List<VoyageToMoonResult> result = new ArrayList<>();
        while(t.before(endTime)) {
            for(int speed=parameters.startSpeed; speed<parameters.endSpeed; speed++) {
            	VoyageToMoonResult input = new VoyageToMoonResult();
            	input.setTimestamp(t);
            	input.setSpeed(speed);
            	result.add(input);
            }
            t = t.add(parameters.stepInSeconds);
        }
        return result;
	}
	
	@Override
	protected Predicate<VoyageToMoonResult> createPredicate() {
		return new Predicate<VoyageToMoonResult>() {
			@Override
			public boolean test(VoyageToMoonResult output) {
				return output.getSurface()>120E3 && output.getSurface()<150E3;
			}
		};
	}

}
