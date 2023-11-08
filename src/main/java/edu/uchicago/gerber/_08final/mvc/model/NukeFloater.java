package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.controller.Sound;

import java.awt.*;
import java.util.LinkedList;

public class NukeFloater extends Floater {

	public static final int SPAWN_NUKE_FLOATER = Game.FRAMES_PER_SECOND * 50;
	public NukeFloater() {
		setColor(Color.YELLOW);
		setExpiry(120);
	}

	@Override
	public void remove(LinkedList<Movable> list) {
		super.remove(list);
		Sound.playSound("nuke-up.wav");
		CommandCenter.getInstance().getFalcon().setNukeMeter(Falcon.MAX_NUKE);

	}


}
