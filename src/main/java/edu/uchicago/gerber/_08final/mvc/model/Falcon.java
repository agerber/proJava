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
	public static final int INITIAL_SPAWN_TIME = 46;

	private int shield;
	private int invisible;

	private int showLevel;

	public static final int MAX_SHIELD = 200;

	private boolean thrusting = false;
	public enum TurnState {
		IDLE, LEFT, RIGHT
	}
	private TurnState turnState = TurnState.IDLE;


	//images states
	private static int FALCON = 0; //normal ship
	private static int FALCON_THR = 1; //normal ship thrusting
	private static int FALCON_PRO = 2; //protected ship (green)
	private static int FALCON_PRO_THR = 3; //protected ship (green) thrusting
	private static int FALCON_INVISIBLE = 4; //for pre-spawning



	// ==============================================================
	// CONSTRUCTOR
	// ==============================================================
	
	public Falcon() {

		setTeam(Team.FRIEND);

		//this is the radius of the falcon
		setRadius(37);



		//We use HashMap which has a seek-time of O(1)
		//See the resources directory in the root of this project for pngs.
		//Using constants as keys is safer b/c we know the value exists when we get it later;
		//if we had hard-coded strings here and below, there's a chance we could misspell it below or elsewhere.

    	Map<Integer, BufferedImage> rasterMap = new HashMap<>();
		rasterMap.put(FALCON, loadGraphic("/imgs/fal/falcon125.png") );
		rasterMap.put(FALCON_THR, loadGraphic("/imgs/fal/falcon125_thr.png") );
		rasterMap.put(FALCON_PRO, loadGraphic("/imgs/fal/falcon125_PRO.png") );
		rasterMap.put(FALCON_PRO_THR, loadGraphic("/imgs/fal/falcon125_PRO_thr.png") );
		rasterMap.put(FALCON_INVISIBLE, null );
		setRasterMap(rasterMap);


	}

	//if spawning then make invincible. You can also set conditions for power-up-shields here, etc.
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
		//The falcon is a convenient place to decrement this variable as the falcon reference is in the movFriends list
		//and so its move() method is being called every frame (~40ms); and the falcon reference is never null.
		if (showLevel > 0) showLevel--;

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

		//set local image-state
		int imageState;
		if (invisible > 0){
			imageState = FALCON_INVISIBLE;
		}
		else if (isProtected()){
			if (thrusting) imageState = FALCON_PRO_THR; else imageState = FALCON_PRO;
		}
		else { //not protected
			if (thrusting) imageState = FALCON_THR; else imageState = FALCON;
		}

		//cast (widen the aperture of) the graphics object to gain access to methods of Graphics2D
		//and render the image according to the image-state
		renderRaster((Graphics2D) g, getRasterMap().get(imageState));

		//you can also add vector elements to raster graphics
		//draw cyan shield
//		if (invisible == 0 && isProtected()) {
//			g.setColor(Color.CYAN);
//			g.drawOval(getCenter().x - getRadius(), getCenter().y - getRadius(), getRadius() *2, getRadius() *2);
//		}



	}



} //end class
