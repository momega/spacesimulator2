package com.momega.spacesimulator.model;

/**
 * Created by martin on 7/19/15.
 */
public class Timestamp implements Comparable<Timestamp> {

    /** time in seconds in UTC **/
    private double value;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Timestamp add(double delta) {
        return newTime(value + delta);
    }

    public Timestamp subtract(double delta) {
        return add(-delta);
    }

    public double subtract(Timestamp v) {
        return value - v.getValue();
    }

    public static Timestamp newTime(double value) {
        Timestamp t = new Timestamp();
        t.setValue(value);
        return t;
    }

    public int toInteger() {
        return Double.valueOf(value).intValue();
    }

    @Override
    public int compareTo(Timestamp o) {
        return Double.compare(value, o.getValue());
    }

    public boolean after(Timestamp o) {
        return (compareTo(o) >0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Timestamp timestamp = (Timestamp) o;

        return Double.compare(timestamp.value, value) == 0;

    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }

    @Override
    public String toString() {
        return "t = " + value;
    }
}
