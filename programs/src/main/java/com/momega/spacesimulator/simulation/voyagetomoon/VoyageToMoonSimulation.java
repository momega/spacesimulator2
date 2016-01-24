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
public class VoyageToMoonSimulation extends Simulation<VoyageToMoonFields, VoyageToMoonResult> {
	
	public VoyageToMoonSimulation() {
		super("Voyage To Moon", VoyageToMoonSolver.class);
	}

	@Override
	protected List<VoyageToMoonResult> generateInputs() {
		VoyageToMoonFields fields = getFields();
		Timestamp timestamp = fields.getStartTime();
        Timestamp endTime = fields.getEndTime();
        Timestamp t = timestamp;
        List<VoyageToMoonResult> result = new ArrayList<>();
        while(t.before(endTime)) {
        	double speed = fields.getStartSpeed();
            while(speed<fields.getEndSpeed()) {
            	VoyageToMoonResult input = new VoyageToMoonResult();
            	input.setTimestamp(t);
            	input.setSpeed(speed);
            	result.add(input);
            	speed += fields.getSpeedStep();
            }
            t = t.add(fields.getStepInSeconds());
        }
        return result;
	}
	
	@Override
	protected Predicate<VoyageToMoonResult> createPredicate(VoyageToMoonFields fields) {
		return new Predicate<VoyageToMoonResult>() {
			@Override
			public boolean test(VoyageToMoonResult output) {
				return output.getSurface()>fields.getMinSurface() && output.getSurface()<fields.getMaxSurface();
			}
		};
	}

}
