package edu.uchicago.gerber._08final.mvc.model;

import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

@Data
public class Falcon extends Sprite {

	// ==============================================================
	// FIELDS 
	// ==============================================================
	
	private static final double THRUST = .65;
	private final static int DEGREE_STEP = 9;
	public static final int INITIAL_SPAWN_TIME = 68;

	//use for spawning and protection
	private int spawn;

	private boolean thrusting = false;
	public enum TurnState {
		IDLE, LEFT, RIGHT
	}
	private TurnState turnState = TurnState.IDLE;

	public enum ImageState {
		FALCON, //normal ship
		FALCON_THR, //normal ship thrusting
		FALCON_PRO, //protected ship (green)
		FALCON_PRO_THR //protected ship (green) thrusting
	}


	// ==============================================================
	// CONSTRUCTOR 
	// ==============================================================
	
	public Falcon() {

		setTeam(Team.FRIEND);

		//this is the radius of the falcon
		setRadius(32);



		//We use HashMap which has a seek-time of O(1)
		//See the resources directory in the root of this project for pngs.
		//Using enums as keys is safer b/c we know the value exists when we get it later;
		//if we had hard-coded strings here and below, there's a chance we could misspell it below or elsewhere.

		//If you use a LinkedHashMap, you can also iterate through the map entries in the same order of
		// insertion, which is convenient if you have an animated series of raster images. With animation, call the
		// iterator.next() method at each draw(). See https://www.geeksforgeeks.org/how-to-iterate-linkedhashmap-in-java/
		// Similar to a stream, you can not re-iterate a spent iterator. So, you must reset the iterator after each
		// full cycle if you intend to loop (think Mario walking from Donkey Kong).

		// You can either create your own series, or search for animated series online. See the explosion_series.png
		// file in the resources/imgs/ directory for an example of a series. If you use this series file (or similar),
		// you will need to slice the series into individual pngs, make the png backgrounds transparent, and
		// likely reduce the pixel depth to a level that is both tolerable in terms of pixelation, but small enough to
		// render quickly.


		Map<String, BufferedImage> rasterMap = new HashMap<>();
		rasterMap.put(ImageState.FALCON.toString(), loadGraphic("/imgs/falcon50.png") );
		rasterMap.put(ImageState.FALCON_THR.toString(), loadGraphic("/imgs/falcon50thrust.png") );
		rasterMap.put(ImageState.FALCON_PRO.toString(), loadGraphic("/imgs/falcon50protect.png") );
		rasterMap.put(ImageState.FALCON_PRO_THR.toString(), loadGraphic("/imgs/falcon50protect_thrust.png") );
		setRasterMap(rasterMap);


	}

	//if spawning then make invincible. You can also set conditions for power-up-shields here, etc.
	@Override
	public boolean isProtected() {
		return  spawn > 0;

	}

	// ==============================================================
	// METHODS 
	// ==============================================================
	@Override
	public void move() {
		super.move();

		if (spawn > 0) spawn--;

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

	}

	//this is a raster implementation of draw()
	@Override
	public void draw(Graphics g) {

		//set image-state
		ImageState imageState;
		if (isProtected()){
			if (thrusting) imageState = ImageState.FALCON_PRO_THR; else imageState = ImageState.FALCON_PRO;
		}
		else { //not protected
			if (thrusting) imageState = ImageState.FALCON_THR; else imageState = ImageState.FALCON;
		}

		//cast (widen the aperture of) the graphics object to gain access to methods of Graphics2D
		//and render the image according to the image-state
		renderRaster((Graphics2D) g, getRasterMap().get(imageState.toString()));

		//draw cyan shield, and warn player of impending non-protection
		if (isProtected() && !(spawn <= 21 && spawn % 7 == 0)) {
			//you can add vector elements to raster graphics
			g.setColor(Color.CYAN);
			g.drawOval(getCenter().x - getRadius(), getCenter().y - getRadius(), getRadius() *2, getRadius() *2);
		}



	}



} //end class
