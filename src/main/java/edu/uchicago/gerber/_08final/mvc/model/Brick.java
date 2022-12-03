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
	//we use upperLeftCorner because that is the origin when drawing graphics in Java
	public Brick(Point upperLeftCorner, int size) {

		//currently set to Foe, but you can change the team or create a new team.
		//you can shoot to destroy the wall which yields big points
		setTeam(Team.FOE);

		setCenter(new Point(upperLeftCorner.x + size/2, upperLeftCorner.y + size/2));

		setRadius(size/2);

		//As this sprite does not animate or change state, we could just store a BufferedImage as a member, but
		//since we already have a rasterMap in the Sprite class, we might as well be consistent for all raster sprites
		// and use it.
    	Map<Integer, BufferedImage> rasterMap = new HashMap<>();
		//brick from Mario Bros
		rasterMap.put(BRICK_IMAGE, loadGraphic("/imgs/brick/Brick_Block100.png") );

		setRasterMap(rasterMap);


	}

	@Override
	public void draw(Graphics g) {
		renderRaster((Graphics2D) g, getRasterMap().get(BRICK_IMAGE));
		//if you uncomment these, you can see how collision works. Feel free to remove these two lines.
		//g.setColor(Color.LIGHT_GRAY);
		//g.drawOval(getCenter().x - getRadius(), getCenter().y - getRadius(), getRadius() *2, getRadius() *2);
	}

} //end class