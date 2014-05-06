package com.smartapps.accel;

import java.util.ArrayList;

/**
 * Created by admin on 5/6/14.
 */
public class GroupData {

    private int nrOfWalks;
    private int nrOfIdle;
    private int nrOfRuns;
    private ArrayList<AccelData> testGroupData;

    public GroupData(int nrOfWalks, int nrOfIdle, int nrOfRuns,ArrayList<AccelData> testGroupData) {

        this.nrOfWalks = nrOfWalks;
        this.nrOfIdle = nrOfIdle;
        this.nrOfRuns = nrOfRuns;
        this.testGroupData = testGroupData;
    }
    // Getters & Setters
    public long getnrofWalks() {
        return this.nrOfWalks;
    }
    public void setnrOfWalks(int nrOfWalks) {
        this.nrOfWalks = nrOfWalks;
    }

    public long getnrofIdle() {
        return this.nrOfIdle;
    }
    public void setnrOfIdle(int nrOfIdle) {
        this.nrOfIdle = nrOfIdle;
    }

    public long getnrofRuns() {
        return this.nrOfRuns;
    }
    public void setnrOfRuns(int nrOfRuns) {
        this.nrOfRuns = nrOfRuns;
    }

}
