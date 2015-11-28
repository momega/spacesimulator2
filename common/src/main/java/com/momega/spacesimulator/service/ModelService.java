package com.momega.spacesimulator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.KeplerianObject;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import com.momega.spacesimulator.model.Spacecraft;

/**
 * Created by martin on 7/25/15.
 */
@Component
public class ModelService {

    public List<CelestialBody> findAllCelestialBodies(Model model) {
        List<CelestialBody> result = new ArrayList<CelestialBody>();
        for(MovingObject mo : model.getMovingObjects()) {
            if (mo instanceof CelestialBody) {
                result.add((CelestialBody) mo);
            }
        }
        return result;
    }

    public List<Spacecraft> findAllSpacecrafts(Model model) {
        List<Spacecraft> result = new ArrayList<Spacecraft>();
        for(MovingObject mo : model.getMovingObjects()) {
            if (mo instanceof Spacecraft) {
                result.add((Spacecraft) mo);
            }
        }
        return result;
    }

    /**
     * Finds the name of the keplerian object
     * @param model the model
     * @param name the name
     * @return the keplerian object
     */
    public MovingObject findByName(Model model, String name) {
        for(MovingObject mo : model.getMovingObjects()) {
            if (name.equals(mo.getName())) {
                return mo;
            }
        }
        return null;
    }

    public List<KeplerianObject> findAllKeplerianObjects(Model model) {
        List<KeplerianObject> result = new ArrayList<KeplerianObject>();
        for(MovingObject mo : model.getMovingObjects()) {
            if (mo instanceof KeplerianObject) {
                result.add((KeplerianObject) mo);
            }
        }
        return result;
    }

}
