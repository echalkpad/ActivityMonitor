package com.smartapps.accel;

/**
 * Created by admin on 5/3/14.
 */
public class Neighbour {
    private AccelData neighbour;
    private double distance;

    // getters and setters
    public void setNeighbour(AccelData neighbour) {
        this.neighbour = neighbour;
    }
    public AccelData getNeighbour() {
        return neighbour;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
    public double getDistance() {
        return distance;
    }

    public Neighbour(AccelData neighbour, double distance) {
        this.neighbour = neighbour;
        this.distance = distance;
    }

}
