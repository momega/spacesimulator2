package com.momega.spacesimulator.model;

/**
 * Created by martin on 7/19/15.
 */
public abstract class MovingObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MovingObject{" +
                "name='" + name + '\'' +
                '}';
    }
}
