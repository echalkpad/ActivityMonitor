package com.smartapps.accel;

import java.util.Comparator;


public class Neighbour implements Comparable<Neighbour>{
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

    public int compareTo(Neighbour n) {
        if (this.distance < n.getDistance()) return -1;
        if (this.distance > n.getDistance()) return 1;
        return 0;
    }


    /**
     * Beautiful comparator
     */
        public static Comparator<Neighbour> NeighbourNameComparator
                = new Comparator<Neighbour>() {

            public int compare(Neighbour n1, Neighbour n2) {
                if(n1.getDistance() < n2.getDistance()) return -1;
                if(n1.getDistance() > n2.getDistance()) return 1;
                return 0;
            }

        };

}
