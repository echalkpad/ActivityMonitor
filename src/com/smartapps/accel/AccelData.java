package com.smartapps.accel;

import android.os.Parcelable;

import javax.vecmath.Point3d;
import java.io.Serializable;
import java.util.ArrayList;


public class AccelData implements Serializable{
	private long timestamp;
    private String label;
	private double x;
	private double y;
	private double z;
    private Point3d point3d;
    public ArrayList<Neighbour> neighbours;


    public AccelData(long timestamp, Point3d point3d, String label) {
        this.timestamp = timestamp;
        this.label = label;
        this.x = x;
        this.y = y;
        this.z = z;
        this.point3d = point3d;
       // this.neighbours = neighbours;
    }
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
    public String getLabel() {return label;}
    public void setLabel(String label) {this.label= label;}
    public Point3d getPoint3D() {return point3d;}
    // getters and setters
    public void setNeighbours(ArrayList<Neighbour> neighbours) {
        this.neighbours = neighbours;
    }
    public ArrayList<Neighbour> getNeighbours() {
        return neighbours;
    }

	
	public String toString()
	{
		return "t="+timestamp+", x="+point3d.getX()+", y="+point3d.getY()+", z="+point3d.getZ();
	}
	

}
