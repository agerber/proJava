package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.controller.SoundLoader;

import java.awt.*;
import java.util.LinkedList;

public class FreezeFloater extends Floater {
	//spawn every 25 seconds
	public static final int SPAWN_SHIELD_FLOATER = Game.FRAMES_PER_SECOND * 25;
	public FreezeFloater() {
		setColor(Color.ORANGE);
		setExpiry(260);
	}

	@Override
	public void removeFromGame(LinkedList<Movable> list) {
		super.removeFromGame(list);
		//if getExpiry() > 0, then this remove was the result of a collision, rather than natural mortality
		if (getExpiry() > 0) {
			SoundLoader.playSound("shieldup.wav");
		    CommandCenter.getInstance().getFalcon().setFreeze(120);
	   }

	}


}
