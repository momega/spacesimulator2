/**
 * 
 */
package com.momega.spacesimulator.service.builder;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.CartesianState;
import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Propulsion;
import com.momega.spacesimulator.model.Spacecraft;
import com.momega.spacesimulator.model.Timestamp;

/**
 * @author martin
 *
 */
@Component
@Scope("prototype")
public class VoyageToMoonBuilder extends EarthMoonBuilder {
	
	private double speed; 

	@Override
    protected void buildModel() {
		super.buildModel();
        
        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setName("Satellite");
        spacecraft.setInitialMass(28445);
        
        Propulsion propulsion = new Propulsion();
        propulsion.setMassFlow(30);
        propulsion.setSpecificImpulse(311);
        propulsion.setTotalFuel(28000);
        spacecraft.setPropulsion(propulsion);
        
        model.getMovingObjects().add(spacecraft);
	}
	
	@Override
	protected void initSpacecraft(Spacecraft spacecraft, Timestamp timestamp) {
		CelestialBody earth = (CelestialBody) modelService.findByName(model, "Earth");
        CartesianState cartesianState = constructCartesianState(earth, spacecraft, timestamp, 300 * 1E3 + earth.getRadius(), 0, 6.0, 125, 138, speed);
        computeSpacecraftInstant(spacecraft, cartesianState, timestamp);
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
