package edu.uchicago.gerber._08final.mvc.model;



import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;

import java.awt.*;
import java.util.LinkedList;

/**
 * Inspired by Michael Vasiliou's Sinistar, winner of Java game contest 2016.
 */
public class MiniMap extends Sprite {
     //size of mini-map as percentage of view-port
    private static final double MINI_MAP_PERCENT = 0.31;

    public MiniMap() {
        setTeam(Team.DEBRIS);
    }

    public void move() {}

    public void draw(final Graphics g) {

       int miniWidth = (int) Math.round(MINI_MAP_PERCENT * Game.DIM.width);
       int miniHeight = (int) Math.round(MINI_MAP_PERCENT * Game.DIM.height);

        //black background (entire universe)
        g.setColor(Color.BLACK);
        g.fillRect(
                0,
                1, //adjust one pixel down
                miniWidth,
                miniHeight
        );

        //blue bounding box (entire universe)
        g.setColor(Color.BLUE);
        g.drawRect(
                0,
                1, //adjust one pixel down
                miniWidth,
                miniHeight
        );


        //blue bounding box (view-port or players view of universe)
       // Point centerOfMiniMap = new Point(miniWidth / 2, miniHeight / 2);
        g.drawRect(
                0 ,
                1, //adjust one pixel down
                miniWidth / Game.UNIVERSE_SCALAR,
                miniHeight / Game.UNIVERSE_SCALAR

        );


        drawRadarBlips(g, Color.RED, CommandCenter.getInstance().getMovFoes());



    }

    private void drawRadarBlips(final Graphics g, Color color, LinkedList<Movable> movables){

        g.setColor(color);
        movables.forEach( mov -> {
                    Point scaledPoint = new Point(
                            (int) Math.round(MINI_MAP_PERCENT * mov.getCenter().x / Game.UNIVERSE_SCALAR),
                            (int) Math.round(MINI_MAP_PERCENT *  mov.getCenter().y / Game.UNIVERSE_SCALAR)
                    );
                    g.fillOval(scaledPoint.x - 2, scaledPoint.y - 2, 4, 4);
                }

        );

    }
}
