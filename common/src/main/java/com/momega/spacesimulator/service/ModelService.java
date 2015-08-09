package com.momega.spacesimulator.service;

import com.momega.spacesimulator.model.CelestialBody;
import com.momega.spacesimulator.model.Model;
import com.momega.spacesimulator.model.MovingObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by martin on 7/25/15.
 */
@Component
public class ModelService {

    public List<CelestialBody> findAllCelestialBodies(Model model) {
        List<CelestialBody> result = model.getMovingObjects().stream().
                filter(movingObject -> movingObject instanceof CelestialBody).
                map(movingObject -> (CelestialBody) movingObject).
                collect(Collectors.toList());
        return result;
    }

}
