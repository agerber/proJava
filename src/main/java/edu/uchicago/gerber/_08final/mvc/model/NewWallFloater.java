package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.Game;

import java.awt.*;

public class NewWallFloater extends Floater {

	private static final Color MAROON = new Color(186, 0, 22);
	//spawn every 40 seconds
	public static final int SPAWN_NEW_WALL_FLOATER = Game.FRAMES_PER_SECOND * 40;
	public NewWallFloater() {
		setColor(MAROON);
		setExpiry(230);
	}





}
