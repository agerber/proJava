package edu.uchicago.gerber._08final.mvc.model;

import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.awt.Point;

public class Brick extends Sprite {

	private final int BRICK_IMAGE = 0;

	//The size of this brick is always square!
	//we use upperLeftCorner because that is the origin when drawing objects in Java
	public Brick(Point upperLeftCorner, int size) {

		//currently set to Friend, but you can change the team or create a new team.
		//Asteroids will crash into this wall when set to FRIEND.
		setTeam(Team.FRIEND);

		setCenter(new Point(upperLeftCorner.x + size/2, upperLeftCorner.y + size/2));

		setRadius(size/2);

    	Map<Integer, BufferedImage> rasterMap = new HashMap<>();
		//brick from Mario Bros
		rasterMap.put(BRICK_IMAGE, loadGraphic("/imgs/brick/Brick_Block100.png") );

		setRasterMap(rasterMap);


	}

	@Override
	public void draw(Graphics g) {
		renderRaster((Graphics2D) g, getRasterMap().get(BRICK_IMAGE));

	}

} //end class
