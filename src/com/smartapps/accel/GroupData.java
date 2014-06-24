package com.smartapps.accel;

import java.util.ArrayList;


/**
 * Class GroupData
 * This class has the group of data obtain with the accelerometer
 * Its also used to save the number of walks, idle and runs after the computation
 */
public class GroupData {

    public static final int NR_INIT_QUANT = -1;
    private int nrOfWalks;
    private int nrOfIdle;
    private int nrOfRuns;
    private ArrayList<AccelData> testGroupData;

    public GroupData(ArrayList<AccelData> testGroupData) {

        this.nrOfWalks = NR_INIT_QUANT;
        this.nrOfIdle = NR_INIT_QUANT;
        this.nrOfRuns = NR_INIT_QUANT;
        this.testGroupData = testGroupData;
    }
    public GroupData(GroupData group) {
        this.nrOfWalks = group.getnrofWalks();
        this.nrOfIdle = group.getnrofIdle();
        this.nrOfRuns = group.getnrofRuns();
        this.testGroupData = group.getData();
    }
    // Getters & Setters
    public int getnrofWalks() {
        return this.nrOfWalks;
    }
    public void setnrOfWalks(int nrOfWalks) {
        this.nrOfWalks = nrOfWalks;
    }

    public int getnrofIdle() {
        return this.nrOfIdle;
    }
    public void setnrOfIdle(int nrOfIdle) {
        this.nrOfIdle = nrOfIdle;
    }

    public int getnrofRuns() {
        return this.nrOfRuns;
    }
    public void setnrOfRuns(int nrOfRuns) {
        this.nrOfRuns = nrOfRuns;
    }

    public void setData(ArrayList<AccelData> testGroupData){
        this.testGroupData = testGroupData;
    }

    public ArrayList<AccelData> getData(){
        return this.testGroupData;
    }
}
