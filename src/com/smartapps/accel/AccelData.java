package com.smartapps.accel;

public class AccelData {
	private long timestamp;
    private String label;
	private double x;
	private double y;
	private double z;



    public AccelData(long timestamp, double x, double y, double z, String label) {
        this.timestamp = timestamp;
        this.label = label;
        this.x = x;
        this.y = y;
        this.z = z;
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
	
	public String toString()
	{
		return "t="+timestamp+", x="+x+", y="+y+", z="+z;
	}
	

}
