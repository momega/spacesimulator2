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
				return m.eccentricity<0.7 && m.perilune>70E3 && m.perilune<150E3;
			}
		};
	}

	@Override
	protected List<MoonOrbitResult> generateInputs() {
		MoonOrbitParameters parameters = getParameters();
		double speed = parameters.speed;
		Timestamp startTime = parameters.timestamp;
		Timestamp minTime = parameters.minTime;
		
        List<MoonOrbitResult> inputs = new ArrayList<>();
        //for(int burnTime=parameters.minBurnTime; burnTime<parameters.maxBurnTime; burnTime+=parameters.burnTimeStep) {
    		MoonOrbitResult mor = new MoonOrbitResult();
    		mor.timestamp = startTime;
    		mor.speed = speed;
    		mor.startBurnTime = minTime;
    		mor.burnTime = parameters.minBurnTime;
    		inputs.add(mor);
    	//}
        return inputs;
	}

}
