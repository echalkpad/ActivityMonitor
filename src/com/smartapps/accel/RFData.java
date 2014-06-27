package com.smartapps.accel;

import javax.vecmath.Point3d;

/**
 * Class RFData
 * It has the basic information (timestamp, ssid and rssi)
 */
public class RFData {


    private long timestamp;
    private String ssid;
    private int rssi;
    //private Point3d point3d;


    public RFData(long timestamp, String ssid, int rssi) {
        this.timestamp = timestamp;
        //this.point3d = point3d;
        this.ssid = ssid;
        this.rssi = rssi;


    }

    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public String toString()
    {
        return "t="+timestamp + " RSSI =" + this.rssi +" SSID ="+this.ssid;
    }

}

