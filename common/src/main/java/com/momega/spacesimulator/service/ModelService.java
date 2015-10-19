package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 7/25/15.
 */
@Component
public class ModelService {

    public List<CelestialBody> findAllCelestialBodies(Model model) {
        List<CelestialBody> result = new ArrayList();
        for(MovingObject mo : model.getMovingObjects()) {
            if (mo instanceof CelestialBody) {
                result.add((CelestialBody) mo);
            }
        }
        return result;
    }

    public List<Spacecraft> findAllSpacecrafts(Model model) {
        List<Spacecraft> result = new ArrayList();
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
    public KeplerianObject findByName(Model model, String name) {
        List<KeplerianObject> result = new ArrayList();
        for(MovingObject mo : model.getMovingObjects()) {
            if (mo instanceof KeplerianObject) {
                if (name.equals(mo.getName())) {
                    return (KeplerianObject) mo;
                }
            }
        }
        return null;
    }

    public List<KeplerianObject> findAllKeplerianObjects(Model model) {
        List<KeplerianObject> result = new ArrayList();
        for(MovingObject mo : model.getMovingObjects()) {
            if (mo instanceof KeplerianObject) {
                result.add((KeplerianObject) mo);
            }
        }
        return result;
    }

}
