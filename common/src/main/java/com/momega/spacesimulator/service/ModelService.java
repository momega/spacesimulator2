package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 7/25/15.
 */
@Component
public class ModelService {

    public List<CelestialBody> findAllCelestialBodies(Model model) {
        List<CelestialBody> result = new ArrayList<>();
        for(MovingObject movingObject : model.getMovingObjects()) {
            if (movingObject instanceof CelestialBody) {
                result.add((CelestialBody) movingObject);
            }
        }
        return result;
    }

}
