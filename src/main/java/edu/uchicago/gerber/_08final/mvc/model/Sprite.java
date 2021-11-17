package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.Game;
import javafx.geometry.Point2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Sprite implements Movable {
	//the center-point of this sprite
	private Point pntCenter;
	//this causes movement; change in x and change in y
	private double deltaX, deltaY;

	private Team team;

	//the radius of circumscribing circle
	private int rad;

	private int orientation;
	private int expiry; //natural mortality (short-living objects)
	//the color of this sprite
	private Color col;

	/*
	todo we don't need these members, they can be calculated from the cartesian
	coords.
	 */
	//radial coordinates
	//this game uses radial coordinates to render sprites
//	public double[] dLengths;
//	public double[] dDegrees;
	

	//fade value for fading in and out
	/*
	todo use a long instead of nFade, and call it instantiateTime and set it to the
	System.currentTimeMillis(). Calculate any fade that way.
	 */
	private int nFade;

	//these are used to draw the polygon. You don't usually need to interface with these
	private Point[] pntCoords; //an array of points used to draw polygon

	/*
	todo these are likewise redundant. Use the cartesian pntCoords instead.
	 */
//	private int[] nXCoords;
//	private int[] nYCoords;


	@Override
	public Team getTeam() {
		//default
	  return team;

	}

	public void setTeam(Team team){
		this.team = team;
	}

	public void move() {

		Point pnt = getCenter();
		double newXPos = pnt.x + getDeltaX();
		double newYPos = pnt.y + getDeltaY();
		
		//this just keeps the sprite inside the bounds of the frame
		if (pnt.x > Game.DIM.width) {
			setCenter(new Point(1, pnt.y));

		} else if (pnt.x < 0) {
			setCenter(new Point(Game.DIM.width - 1, pnt.y));
		} else if (pnt.y > Game.DIM.height) {
			setCenter(new Point(pnt.x, 1));

		} else if (pnt.y < 0) {
			setCenter(new Point(pnt.x, Game.DIM.height - 1));
		} else {

			setCenter(new Point((int) newXPos, (int) newYPos));
		}

	}

	public Sprite() {

		setColor(Color.white);
		//place the sprite at some random location in the frame at instantiation
		setCenter(new Point(Game.R.nextInt(Game.DIM.width),
				Game.R.nextInt(Game.DIM.height)));


	}

	public void setExpire(int n) {
		expiry = n;

	}

//	public double[] getLengths() {
//		return this.dLengths;
//	}
//
//	public void setLengths(double[] dLengths) {
//		this.dLengths = dLengths;
//	}
//
//	public double[] getDegrees() {
//		return this.dDegrees;
//	}
//
//	public void setDegrees(double[] dDegrees) {
//		this.dDegrees = dDegrees;
//	}

	public Color getColor() {
		return col;
	}

	public void setColor(Color col) {
		this.col = col;

	}

	public int points() {
		//default is zero
		return 0;
	}

	//todo possible candidate for Movable interface
	public int getExpire() {
		return expiry;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int n) {
		orientation = n;
	}

	public void setDeltaX(double dSet) {
		deltaX = dSet;
	}

	public void setDeltaY(double dSet) {
		deltaY = dSet;
	}

	public double getDeltaY() {
		return deltaY;
	}

	public double getDeltaX() {
		return deltaX;
	}

	public int getRadius() {
		return rad;
	}

	public void setRadius(int n) {
		rad = n;

	}


	public Point getCenter() {
		return pntCenter;
	}

	public void setCenter(Point pntParam) {
		pntCenter = pntParam;
	}


//	public void setYcoord(int nValue, int nIndex) {
//		nYCoords[nIndex] = nValue;
//	}
//
//	public void setXcoord(int nValue, int nIndex) {
//		nXCoords[nIndex] = nValue;
//	}
//
//
//	public int getYcoord( int nIndex) {
//		return nYCoords[nIndex];// = nValue;
//	}
//
//	public int getXcoord( int nIndex) {
//		return nXCoords[nIndex];// = nValue;
//	}
//
//
//
//	public int[] getXcoords() {
//		return nXCoords;
//	}
//
//	public int[] getYcoords() {
//		return nYCoords;
//	}
//
//
//	public void setXcoords( int[] nCoords) {
//		 nXCoords = nCoords;
//	}
//
//	public void setYcoords(int[] nCoords) {
//		 nYCoords =nCoords;
//	}

	protected double hypot(double dX, double dY) {
		return Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
	}

	protected Point[] polarToCartesian(double[] degrees, double[] lengths){

		Point[] pnts = new Point[degrees.length];
		for (int nC = 0; nC < degrees.length; nC++) {
			double angleInRadians = Math.toRadians(degrees[nC]);
			int x = (int) (Math.round(lengths[nC] * Math.cos(angleInRadians) * 100f) / 100f);
			int y = (int) (Math.round(lengths[nC] * Math.sin(angleInRadians) * 100f) / 100f);
			pnts[nC] = new Point(x,y);
		}
		return pnts;
		
	}
	
	//utility function to convert from cartesian to polar
	//since it's much easier to describe a sprite as a list of cartesean points
	//sprites (except Asteroid) should use the cartesean technique to describe the coordinates
	//see Falcon or Bullet constructor for examples

	//this can be done on the fly, do not assign to members
	protected double[] convertToPolarDegs(List<Point> pntPoints) {

	   double[] dDegs = new double[pntPoints.size()];

		int nC = 0;
		for (Point pnt : pntPoints) {
			dDegs[nC++]=(Math.toDegrees(Math.atan2(pnt.y, pnt.x))) * Math.PI / 180 ;
		}
		return dDegs;
	}
	//utility function to convert to polar
	protected double[] convertToPolarLens(List<Point> pntPoints) {

		double[] dLens = new double[pntPoints.size()];

		//determine the largest hypotenuse
		double dL = 0;
		for (Point pnt : pntPoints)
			if (hypot(pnt.x, pnt.y) > dL)
				dL = hypot(pnt.x, pnt.y);

		int nC = 0;
		for (Point pnt : pntPoints) {
			if (pnt.x == 0 && pnt.y > 0) {
				dLens[nC] = hypot(pnt.x, pnt.y) / dL;
			} else if (pnt.x < 0 && pnt.y > 0) {
				dLens[nC] = hypot(pnt.x, pnt.y) / dL;
			} else {
				dLens[nC] = hypot(pnt.x, pnt.y) / dL;
			}
			nC++;
		}

		// holds <thetas, lens>
		return dLens;

	}

//	protected void assignPolarPoints(List<Point> pntCs) {
//		setDegrees(convertToPolarDegs(pntCs));
//		setLengths(convertToPolarLens(pntCs));
//
//	}

	//todo reengineer this so that draw converts pntCoords to xCoords, yCoords on the fly locally, and NOT as members.
	@Override
    public void draw(Graphics g) {
//        nXCoords = new int[dDegrees.length];
//        nYCoords = new int[dDegrees.length];
//        //need this as well
//        pntCoords = new Point[dDegrees.length];
//
//
//        for (int nC = 0; nC < dDegrees.length; nC++) {
//            nXCoords[nC] =    (int) (getCenter().x + getRadius()
//                            * dLengths[nC]
//                            * Math.sin(Math.toRadians(getOrientation()) + dDegrees[nC]));
//            nYCoords[nC] =    (int) (getCenter().y - getRadius()
//                            * dLengths[nC]
//                            * Math.cos(Math.toRadians(getOrientation()) + dDegrees[nC]));
//
//
//            //need this line of code to create the points which we will need for debris
//            pntCoords[nC] = new Point(nXCoords[nC], nYCoords[nC]);
//        }

        g.setColor(getColor());
        //Arrays.stream(pntCoords).map(pnt -> pnt.getX()).collect(Collectors.toList()).toArray()
        g.drawPolygon(
				convertStreamToArray(Arrays.stream(pntCoords).map(pnt -> (int) pnt.getX())),
				convertStreamToArray(Arrays.stream(pntCoords).map(pnt -> (int) pnt.getY())),
				pntCoords.length);
    }

	protected int[] convertStreamToArray(Stream<Integer> stream) {
		return stream.mapToInt(Integer::intValue).toArray();
	}
    

	public Point[] getObjectPoints() {
		return pntCoords;
	}
	
	public void setObjectPoints(Point[] pntPs) {
		 pntCoords = pntPs;
	}

	public void setObjectPoints(List<Point> pntPs) {

		pntCoords = pntPs.stream()
				.toArray(Point[]::new);
	}
	
	public void setObjectPoint(Point pnt, int nIndex) {
		 pntCoords[nIndex] = pnt;
	}

	public int getFadeValue() {
		return nFade;
	}

	public void setFadeValue(int n) {
		nFade = n;
	}

}
