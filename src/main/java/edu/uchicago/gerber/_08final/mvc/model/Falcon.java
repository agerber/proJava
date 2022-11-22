package edu.uchicago.gerber._08final.mvc.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Falcon extends Sprite {

	// ==============================================================
	// FIELDS 
	// ==============================================================
	
	private static final double THRUST = .65;
	private final static int DEGREE_STEP = 9;
	//must be multiple of 3
	public static final int FADE_INITIAL_VALUE = 51;
	
	//private boolean shield = false;
	private boolean thrusting = false;
	public enum TurnState {
		IDLE, LEFT, RIGHT
	}
	private TurnState turnState = TurnState.IDLE;

	private final Point[] pntShip, pntAlien;



	// ==============================================================
	// CONSTRUCTOR 
	// ==============================================================
	
	public Falcon() {

		setTeam(Team.FRIEND);

		//this is the size (radius) of the falcon
		setRadius(35);


		List<Point> listShip = new ArrayList<>();
		// Robert Alef's awesome falcon design
		listShip.add(new Point(0,9));
		listShip.add(new Point(-1, 6));
		listShip.add(new Point(-1,3));
		listShip.add(new Point(-4, 1));
		listShip.add(new Point(4,1));
		listShip.add(new Point(-4,1));
		listShip.add(new Point(-4, -2));
		listShip.add(new Point(-1, -2));
		listShip.add(new Point(-1, -9));
		listShip.add(new Point(-1, -2));
		listShip.add(new Point(-4, -2));
		listShip.add(new Point(-10, -8));
		listShip.add(new Point(-5, -9));
		listShip.add(new Point(-7, -11));
		listShip.add(new Point(-4, -11));
		listShip.add(new Point(-2, -9));
		listShip.add(new Point(-2, -10));
		listShip.add(new Point(-1, -10));
		listShip.add(new Point(-1, -9));
		listShip.add(new Point(1, -9));
		listShip.add(new Point(1, -10));
		listShip.add(new Point(2, -10));
		listShip.add(new Point(2, -9));
		listShip.add(new Point(4, -11));
		listShip.add(new Point(7, -11));
		listShip.add(new Point(5, -9));
		listShip.add(new Point(10, -8));
		listShip.add(new Point(4, -2));
		listShip.add(new Point(1, -2));
		listShip.add(new Point(1, -9));
		listShip.add(new Point(1, -2));
		listShip.add(new Point(4,-2));
		listShip.add(new Point(4, 1));
		listShip.add(new Point(1, 3));
		listShip.add(new Point(1,6));
		listShip.add(new Point(0,9));



		//Danica Gutierrez' crazy Alien
		List<Point> listAlien = new ArrayList<>();
		listAlien.add(new Point(0,2));
		listAlien.add(new Point(1,2));
		listAlien.add(new Point(1,3));
		listAlien.add(new Point(2,3));
		listAlien.add(new Point(2,4));
		listAlien.add(new Point(3,4));
		listAlien.add(new Point(3,3));
		listAlien.add(new Point(2, 3));
		listAlien.add(new Point(2,2));
		listAlien.add(new Point(3, 2));
		listAlien.add(new Point(3,1));
		listAlien.add(new Point(4, 1));
		listAlien.add(new Point(4,0));
		listAlien.add(new Point(5, 0));
		//bottom right
		listAlien.add(new Point(5, 0));
		listAlien.add(new Point(5,-3));
		listAlien.add(new Point(4, -3));
		listAlien.add(new Point(4,-1));
		listAlien.add(new Point(3, -1));
		listAlien.add(new Point(3,-3));
		listAlien.add(new Point(2, -3));
		listAlien.add(new Point(2,-4));
		listAlien.add(new Point(1,-4));
		listAlien.add(new Point(1,-3));
		listAlien.add(new Point(2,-3));
		listAlien.add(new Point(2,-2));
		listAlien.add(new Point(1,-2));
		listAlien.add(new Point(0,-2));
		//bottom left quadrant
		listAlien.add(new Point(-2,-2));
		listAlien.add(new Point(-2,-3));
		listAlien.add(new Point(-1,-3));
		listAlien.add(new Point(-1,-4));
		listAlien.add(new Point(-2,-4));
		listAlien.add(new Point(-2, -3));
		listAlien.add(new Point(-3,-3));
		listAlien.add(new Point(-3, -1));
		listAlien.add(new Point(-4,-1));
		listAlien.add(new Point(-4, -3));
		listAlien.add(new Point(-5,-3));
		listAlien.add(new Point(-5, 0));
		//top left quadrant
		listAlien.add(new Point(-5, 0));
		listAlien.add(new Point(-4,0));
		listAlien.add(new Point(-4, 1));
		listAlien.add(new Point(-3,1));
		listAlien.add(new Point(-3, 2));
		listAlien.add(new Point(-2,2));
		listAlien.add(new Point(-2, 3));
		listAlien.add(new Point(-3,3));
		listAlien.add(new Point(-3,4));
		listAlien.add(new Point(-2,4));
		listAlien.add(new Point(-2,3));
		listAlien.add(new Point(-1,3));
		listAlien.add(new Point(-1,2));
		listAlien.add(new Point(0,2));



		//create an alternative shape for alien
		pntAlien = pointsListToArray(listAlien);
		//we need to store the default points as member (unlike other sprites) because we toggle alien
		pntShip = pointsListToArray(listShip);
		//set initial default to normal (ship) points
		setCartesians(pntShip);


	}

	//has no functional use, but demonstrates how to morph the shape of a Sprite
	public void toggleAlien(boolean alien){
			if (alien){
				setCartesians(pntAlien);
			} else {
				setCartesians(pntShip);
			}
	}


	@Override
	public boolean isProtected() {
		return getFade() < 255;
	}

	// ==============================================================
	// METHODS 
	// ==============================================================
	@Override
	public void move() {
		super.move();

		if (isProtected()) {
			setFade(getFade() + 3);
		}

		//apply some thrust vectors using trig.
		if (thrusting) {
			double adjustX = Math.cos(Math.toRadians(getOrientation()))
					* THRUST;
			double adjustY = Math.sin(Math.toRadians(getOrientation()))
					* THRUST;
			setDeltaX(getDeltaX() + adjustX);
			setDeltaY(getDeltaY() + adjustY);
		}

		switch (turnState){
			case LEFT:
				if (getOrientation() <= 0) {
					setOrientation(360);
				}
				setOrientation(getOrientation() - DEGREE_STEP);
				break;
			case RIGHT:
				if (getOrientation() >= 360) {
					setOrientation(0);
				}
				setOrientation(getOrientation() + DEGREE_STEP);
				break;
			default:
				//do nothing

		}

	} //end move



	//methods for moving the falcon
	public void rotateLeft() {
		turnState = TurnState.LEFT;
	}

	public void rotateRight() {
		turnState = TurnState.RIGHT;
	}

	public void stopRotating() {
		turnState = TurnState.IDLE;

	}

	public void thrustOn() {
		thrusting = true;
	}

	public void thrustOff() {
		thrusting = false;
	}



	private int adjustColor(int colorNum, int adjust) {
		return Math.max(colorNum - adjust, 0);
	}

	@Override
	public void draw(Graphics g) {

		Color colShip;
		if (getFade() == 255) {
			colShip = getColor(); //get native color of the sprite
		}
		//flash to warn player of impending non-protection
		else if (getFade() > 220 && getFade() % 9 == 0 ){
			colShip = new Color(0, 32, 128); //dark blue
		}
		//some increasingly lighter shade of blue
		else {
			colShip = new Color(

					adjustColor(getFade(), 200), //red
					adjustColor(getFade(), 175), //green
					getFade() //blue
			);
		}

		//most Sprites do not have flames, but Falcon does
		 double[] flames = { 23 * Math.PI / 24 + Math.PI / 2, Math.PI + Math.PI / 2, 25 * Math.PI / 24 + Math.PI / 2 };
		 Point[] pntFlames = new Point[flames.length];

		//thrusting
		if (thrusting) {
			//the flame
			for (int nC = 0; nC < flames.length; nC++) {
				if (nC % 2 != 0) //odd
				{
					//adjust the position so that the flame is off-center
					pntFlames[nC] = new Point((int) (getCenter().x + 2
							* getRadius()
							* Math.sin(Math.toRadians(getOrientation())
									+ flames[nC])), (int) (getCenter().y - 2
							* getRadius()
							* Math.cos(Math.toRadians(getOrientation())
									+ flames[nC])));

				} else //even
				{
					pntFlames[nC] = new Point((int) (getCenter().x + getRadius()
							* 1.1
							* Math.sin(Math.toRadians(getOrientation())
									+ flames[nC])),
							(int) (getCenter().y - getRadius()
									* 1.1
									* Math.cos(Math.toRadians(getOrientation())
											+ flames[nC])));

				} //end even/odd else
			} //end for loop

			g.setColor(colShip); //flames same color as ship
			g.fillPolygon(
					Arrays.stream(pntFlames)
							.map(pnt -> pnt.x)
							.mapToInt(Integer::intValue)
							.toArray(),

					Arrays.stream(pntFlames)
							.map(pnt -> pnt.y)
							.mapToInt(Integer::intValue)
							.toArray(),

					flames.length);

		} //end if flame

		draw(g,colShip);

	} //end draw()

} //end class
