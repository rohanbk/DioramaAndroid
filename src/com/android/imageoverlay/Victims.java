package com.android.imageoverlay;

import android.graphics.Bitmap;

/**
 * 
 * @author Rohan Balakrishnan
 *
 * Victims class is used to hold all POI information
 */

public class Victims {

	/**
	 * x => deviation from the center of the screen
	 * rad => Radius of POI object
	 * description =>Information about POI object
	 * Angle_Offset => Difference in the angle between compass and POI 
	 */
	
	
	private float x; 
	private float y;
	private int rad;
	private int angle;
	private String description;
	private int Angle_Offset;
	private double distance;	
	private int type;
	private Bitmap _scratch;

	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public double getDistance() {
		return distance;
	}


	public void setDistance(double distance) {
		this.distance = distance;
	}


	public Victims(float x, float y, int rad, int angle, String Desc, int angle_offset, double distance,int type)
	{
		this.x =x ;
		this.y = y;
		this.rad = rad;
		this.angle = angle;
		this.description = Desc;
		this.Angle_Offset = angle_offset;
		this.distance = distance;
		this.type=type;
	}
	
	
	public Victims(float x, float y, int rad, int angle, String Desc, 
			int angle_offset, double distance,int type,	Bitmap _scratch) {
		this.x =x ;
		this.y = y;
		this.rad = rad;
		this.angle = angle;
		this.description = Desc;
		this.Angle_Offset = angle_offset;
		this.distance = distance;
		this.type=type;
		
	}


	public void setX(float x)
	{
		this.x = x;
	}
	public void setY(float y)
	{
		this.y = y;
	}
	public void setRad(int rad)
	{
		this.rad = rad;
	}
	public void setAngle(int Angle)
	{
		this.angle = Angle;
	}
	
	public void setDescription(String Description)
	{
		this.description = Description;
	}
	
	public float getX()
	{
		return this.x;
	}
	public float getY()
	{
		return this.y;
	}
	public float getRad()
	{
		return this.rad;
	}
	public int getAngle()
	{
		return this.angle;
	}
	public String getDescription()
	{
		return this.description;
	}
	public int getAngle_Offset() {
		return Angle_Offset;
	}

	public void setAngle_Offset(int angleOffset) {
		Angle_Offset = angleOffset;
	}

	
}
