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
public class MoonOrbitSimulation extends Simulation<MoonOrbitParameters, MoonOrbitResult> {

	protected MoonOrbitSimulation(MoonOrbitParameters parameters) {
		super("Moon Orbit", parameters, MoonOrbitCallable.class);
	}

	@Override
	protected Predicate<MoonOrbitResult> createPredicate() {
		return new Predicate<MoonOrbitResult>() {
			@Override
			public boolean test(MoonOrbitResult m) {
				return m.eccentricity<0.4 && m.perilune>70E3 && m.apolune<1770E3;
			}
		};
	}

	@Override
	protected List<MoonOrbitResult> generateInputs() {
		MoonOrbitParameters parameters = getParameters();
        List<MoonOrbitResult> inputs = new ArrayList<>();
        
		Timestamp t = parameters.startTime;
        while(t.before(parameters.endTime)) {
        	Timestamp bt = parameters.startBurnTime;
        	while (bt.before(parameters.endBurnTime)) {
        		MoonOrbitResult mor = new MoonOrbitResult();
        		mor.timestamp = t;
        		mor.speed = parameters.speed; 
        		mor.startBurnTime = bt;
        		mor.burnTime = parameters.burnTime;
        		inputs.add(mor);
        		
        		bt = bt.add(parameters.stepTime);
        	}
        	t = t.add(parameters.stepTime);
        }
    		
        return inputs;
	}

}
