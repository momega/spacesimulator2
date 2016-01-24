/**
 * 
 */
package com.momega.spacesimulator.simulation.moonorbit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.Timestamp;
import com.momega.spacesimulator.simulation.Simulation;

/**
 * @author martin
 *
 */
@Component
@Scope("prototype")
public class MoonOrbitSimulation extends Simulation<MoonOrbitFields, MoonOrbitResult> {

	protected MoonOrbitSimulation() {
		super("Moon Orbit", MoonOrbitSolver.class);
	}

	@Override
	protected Predicate<MoonOrbitResult> createPredicate(final MoonOrbitFields fields) {
		return new Predicate<MoonOrbitResult>() {
			@Override
			public boolean test(MoonOrbitResult m) {
				return m.eccentricity<fields.getMaxEccentricity() 
						&& m.perilune>fields.getPerilune() && m.apolune<fields.getPerilune();
			}
		};
	}

	@Override
	protected List<MoonOrbitResult> generateInputs() {
		MoonOrbitFields parameters = getFields();
        List<MoonOrbitResult> inputs = new ArrayList<>();
        
		Timestamp t = parameters.getStartTime();
        while(t.before(parameters.getEndTime())) {
        	Timestamp bt = parameters.getStartBurnTime();
        	while (bt.before(parameters.getEndBurnTime())) {
        		MoonOrbitResult mor = new MoonOrbitResult();
        		mor.timestamp = t;
        		mor.speed = parameters.getSpeed();
        		mor.startBurnTime = bt;
        		mor.burnTime = parameters.getBurnTime();
        		inputs.add(mor);
        		
        		bt = bt.add(parameters.getStepTime());
        	}
        	t = t.add(parameters.getStepTime());
        }
    		
        return inputs;
	}

}
