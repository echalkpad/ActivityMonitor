package com.smartapps.accel;

import javax.vecmath.Point3d;
import java.util.ArrayList;

/**
 * Class AccelData
 * This class corresponds to a point obtain with the accelerometer
 * with coordinates x, y and z and a state that can be either Idle, Walk or Run
 * Each point knows about its nearest neighbors
 */
public class AccelData {

    public enum State {
        Idle, Walk, Run,
    }

    private State pointState;

	//private double x;
	//private double y;
	//private double z;
    private Point3d point3d;
    public ArrayList<Neighbour> neighbours;


    /**
     * Construct of Accel Data without knowing the state of the new point

     * @param point3d - coordinates of the point
     */
    public AccelData(Point3d point3d) {

        //this.x = x;
        //this.y = y;
        //this.z = z;
        this.point3d = point3d;
       // this.neighbours = neighbours;
    }

    /**
     * Construct of AccelData with the state of the new point

     * @param point3d - - coordinates of the point
     * @param pointState - state of the point (Idle, Walk or Run)
     */
    public AccelData(Point3d point3d, State pointState) {

        //this.x = x;
        //this.y = y;
        //this.z = z;
        this.point3d = point3d;
        this.pointState = pointState;
        // this.neighbours = neighbours;
    }

	/*********************************
	       Getters and setters
	*********************************/


    public State getPointState(){
        return this.pointState;
    }

    public void setPointState(State newState){
        this.pointState = newState;
    }
	
    public ArrayList<Neighbour> getNeighbours() {
        return neighbours;
    }

    public Point3d getPoint3D() {
		return point3d;
	}
   
    public void setNeighbours(ArrayList<Neighbour> neighbours) {
        this.neighbours = neighbours;
    }
	
	public String toString()
	{
		return "x="+point3d.getX()+", y="+point3d.getY()+", z="+point3d.getZ() + " State is " + this.pointState.name();
	}
	

}
