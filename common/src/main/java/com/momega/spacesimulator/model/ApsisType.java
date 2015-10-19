package com.momega.spacesimulator.model;

/**
 * Apsis Type (periapsis or apoapsis)
 * Created by martin on 6/30/14.
 */
public enum ApsisType {

    PERIAPSIS(0d, "Pe"),
    APOAPSIS(Math.PI, "Ap");

    private final double angle;
    private final String shortcut;

    ApsisType(double angle, String shortcut) {
        this.angle = angle;
        this.shortcut = shortcut;
    }

    public double getTrueAnomaly() {
        return angle;
    }

    public String getShortcut() {
        return shortcut;
    }

}
