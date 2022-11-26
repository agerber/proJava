package edu.uchicago.gerber._08final.mvc.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;


public class Falcon extends Sprite {

	// ==============================================================
	// FIELDS 
	// ==============================================================
	
	private static final double THRUST = .65;
	private final static int DEGREE_STEP = 9;
	//must be multiple of 3
	public static final int INITIAL_SPAWN_TIME = 68;

	private boolean thrusting = false;
	public enum TurnState {
		IDLE, LEFT, RIGHT
	}
	private TurnState turnState = TurnState.IDLE;





	// ==============================================================
	// CONSTRUCTOR 
	// ==============================================================
	
	public Falcon() {

		setTeam(Team.FRIEND);

		//this is the radius of the falcon
		setRadius(35);
		setSpawn(INITIAL_SPAWN_TIME);

		//in this case we are loading the raster graphics
		//see the resources/imgs directory in the root of this project.
		Map<String, BufferedImage> rasters = new HashMap<>();
		rasters.put("NORMAL",loadGraphic("falcon50.png") );
		rasters.put("NORMAL_THRUSTING",loadGraphic("falcon50thrust.png") );
		rasters.put("PROTECTED",loadGraphic("falcon50protect.png") );
		rasters.put("PROTECTED_THRUSTING",loadGraphic("falcon50both.png") );
		setRasters(rasters);



	}


	//if spawning then make invincible. You can also set conditions for power-up shields here, etc.
	@Override
	public boolean isProtected() {
		return  getSpawn() > 0;

	}

	// ==============================================================
	// METHODS 
	// ==============================================================
	@Override
	public void move() {
		super.move();

		if (getSpawn() > 0) setSpawn(getSpawn() -1);

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

	//this is a raster implementation of draw
	@Override
	public void draw(Graphics g) {


		if (isProtected()){
			if (thrusting){
				renderRaster((Graphics2D) g, getRasters().get("PROTECTED_THRUSTING"));
			} else {
				renderRaster((Graphics2D) g, getRasters().get("PROTECTED"));
			}

			//you can add vector elements to raster graphics
			g.setColor(Color.CYAN);
			g.drawOval(getCenter().x - getRadius(), getCenter().y - getRadius(), getRadius() *2, getRadius() *2);

		} else { //not protected
			if (thrusting){
				renderRaster((Graphics2D) g, getRasters().get("NORMAL_THRUSTING"));
			} else {
				renderRaster((Graphics2D) g, getRasters().get("NORMAL"));
			}

		}


	} //end draw()

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




} //end class
