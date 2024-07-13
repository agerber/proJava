package edu.uchicago.gerber._08final.mvc.model;



import edu.uchicago.gerber._08final.mvc.controller.Game;

import java.awt.*;
import java.util.ArrayList;

/**
 * Inspired by Michael Vasiliou's Sinistar, winner of Java game contest 2016.
 */
public class MiniMap extends Sprite {
    private static final int MAP_MARGIN = 20;
    private static final int WIDTH_FACTOR = 5;
    private static final int HEIGHT_FACTOR = 4;

    public MiniMap() {
        setTeam(Team.DEBRIS);
    }

    public void move() {


    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(Game.DIM.width - (Game.DIM.width / WIDTH_FACTOR) - MAP_MARGIN,
                MAP_MARGIN,
                Game.DIM.width / WIDTH_FACTOR + 4,
                Game.DIM.height / HEIGHT_FACTOR + 4);
        g.setColor(Color.BLUE);

        g.drawRect(Game.DIM.width - (Game.DIM.width / WIDTH_FACTOR) - MAP_MARGIN,
                   MAP_MARGIN,
                   Game.DIM.width / WIDTH_FACTOR + 4,
                   Game.DIM.height / HEIGHT_FACTOR + 4);

        g.drawRect(Game.DIM.width - (2 * Game.DIM.width / (3 *WIDTH_FACTOR)) - MAP_MARGIN,
                   MAP_MARGIN + (Game.DIM.height / (3* HEIGHT_FACTOR)),
                   Game.DIM.width / (3 * WIDTH_FACTOR),
                   Game.DIM.height / (3 * HEIGHT_FACTOR));


    }
}
