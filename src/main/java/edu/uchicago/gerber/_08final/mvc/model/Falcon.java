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

	//static fields

	//number of degrees the falcon will turn at each animation cycle
	public final static int TURN_STEP = 12;
	public static final int INITIAL_SPAWN_TIME = 46;
	public static final int MAX_SHIELD = 200;

	public static final int MIN_RADIUS = 32;


	//images states
	public enum ImageState {
		FALCON_INVISIBLE, //for pre-spawning
		FALCON, //normal ship
		FALCON_PRO, //protected ship (green)
		FALCON_THR, //normal ship thrusting
		FALCON_PRO_THR, //protected ship (green) thrusting

	}


	//instance fields (getters/setters provided by Lombok @Data above)
	private int shield;
	private int invisible;

	//showLevel is not germane to the Falcon. Rather, it controls whether the level is shown in the middle of the
	// screen. However, given that the Falcon reference is never null, and that a Falcon is a Sprite whose move/draw
	// methods are being called every ~40ms, this is a very convenient place to store this variable.
	private int showLevel;
	private boolean thrusting;
	//enum used for turnState field
	public enum TurnState {IDLE, LEFT, RIGHT}
	private TurnState turnState = TurnState.IDLE;


	// ==============================================================
	// CONSTRUCTOR
	// ==============================================================
	
	public Falcon() {

		setTeam(Team.FRIEND);

		setRadius(MIN_RADIUS);


		//We use HashMap which has a seek-time of O(1)
		//See the resources directory in the root of this project for pngs.
		//Using enums as keys is safer b/c we know the value exists when we reference the consts later in code.
    	Map<ImageState, BufferedImage> rasterMap = new HashMap<>();
		rasterMap.put(ImageState.FALCON_INVISIBLE, null );
		rasterMap.put(ImageState.FALCON, loadGraphic("/imgs/fal/falcon125.png") ); //normal ship
		rasterMap.put(ImageState.FALCON_THR, loadGraphic("/imgs/fal/falcon125_thr.png") ); //normal ship thrusting
		rasterMap.put(ImageState.FALCON_PRO, loadGraphic("/imgs/fal/falcon125_PRO.png") ); //protected ship (green)
		rasterMap.put(ImageState.FALCON_PRO_THR, loadGraphic("/imgs/fal/falcon125_PRO_thr.png") ); //green thrusting

		setRasterMap(rasterMap);


	}

	@Override
	public boolean isProtected() {
		return  shield > 0;

	}

	// ==============================================================
	// METHODS 
	// ==============================================================
	@Override
	public void move() {
		super.move();

		if (invisible > 0) invisible--;
		if (shield > 0) shield--;
		//The falcon is a convenient place to decrement this variable as the falcon
		//move() method is being called every frame (~40ms); and the falcon reference is never null.
		if (showLevel > 0) showLevel--;

		//apply some thrust vectors using trig.
		final double THRUST = 0.85;
		if (thrusting) {
			double vectorX = Math.cos(Math.toRadians(getOrientation()))
					* THRUST;
			double vectorY = Math.sin(Math.toRadians(getOrientation()))
					* THRUST;
			setDeltaX(getDeltaX() + vectorX);
			setDeltaY(getDeltaY() + vectorY);
		}

		//Make the ship radius bigger when the absolute velocity increases, thereby increasing difficulty when not
		// protected, and allowing player to use the shield offensively when protected.
		//Absolute velocity is the hypotenuse of absolute deltaX and deltaY
		final int ABS_VELOCITY =
				(int) Math.sqrt(Math.pow(Math.abs(getDeltaX()), 2) + Math.pow(Math.abs(getDeltaY()), 2));
		setRadius(MIN_RADIUS + ABS_VELOCITY / 3);

		//adjust the orientation given turnState
		int adjustOr = getOrientation();
		switch (turnState){
			case LEFT:
				adjustOr = getOrientation() <= 0 ? 360 - TURN_STEP : getOrientation() - TURN_STEP;
				break;
			case RIGHT:
				adjustOr = getOrientation() >= 360 ? TURN_STEP : getOrientation() + TURN_STEP;
				break;
			case IDLE:
			default:
				//do nothing
		}
		setOrientation(adjustOr);

	}

	//this is a raster and vector implementation of draw()
	@Override
	public void draw(Graphics g) {

		//set local image-state
		ImageState imageState;
		if (invisible > 0){
			imageState = ImageState.FALCON_INVISIBLE;
		}
		else if (isProtected()){
			imageState = thrusting ? ImageState.FALCON_PRO_THR : ImageState.FALCON_PRO;
			//you can also combine vector elements and raster elements
		    drawShield(g);
		}
		else { //not protected
			imageState = thrusting ? ImageState.FALCON_THR : ImageState.FALCON;
		}

		//cast (widen the aperture of) the graphics object to gain access to methods of Graphics2D
		//and render the raster image according to the image-state
		renderRaster((Graphics2D) g, getRasterMap().get(imageState));

	}

	private void drawShield(Graphics g){
		g.setColor(Color.CYAN);
		g.drawOval(getCenter().x - getRadius(), getCenter().y - getRadius(), getRadius() *2, getRadius() *2);
	}



} //end class
