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
    private static final double MINI_MAP_PERCENT = 0.42;

    public MiniMap() {
        setTeam(Team.DEBRIS);
    }

    public void move() {}

    public void draw(final Graphics g) {

        if (CommandCenter.getInstance().getUniverse() == CommandCenter.Universe.SMALL) return;


        int width = (int) Math.round(MINI_MAP_PERCENT * Game.DIM.width);
        int height = (int) Math.round(MINI_MAP_PERCENT * Game.DIM.height);

        //if BIG - show entire universe.
        if (CommandCenter.getInstance().getUniverse() == CommandCenter.Universe.BIG) {

            g.setColor(Color.BLACK);
            g.fillRect(
                    0,
                    1, //adjust one pixel down
                    width,
                    height
            );

            //gray bounding box (entire universe)
            g.setColor(Color.DARK_GRAY);
            g.drawRect(
                    0,
                    1, //adjust one pixel down
                    width,
                    height
            );
        }

        //mini-view-port gray bounding box (player's view of universe)
        g.setColor(Color.DARK_GRAY);
        int miniViewPortWidth = width / Game.BIG_UNIVERSE_SCALAR;
        int miniViewPortHeight = height / Game.BIG_UNIVERSE_SCALAR;
        g.drawRect(
                0 ,
                1, //adjust one pixel down
                miniViewPortWidth,
                miniViewPortHeight

        );

        //draw the non-debris movables
        drawRadarBlips(g, Color.WHITE, CommandCenter.getInstance().getMovFoes());
        drawRadarBlips(g, Color.CYAN, CommandCenter.getInstance().getMovFloaters());
        drawRadarBlips(g, Color.ORANGE, CommandCenter.getInstance().getMovFriends());


    }

    private void drawRadarBlips(final Graphics g, Color color, LinkedList<Movable> movables){

        g.setColor(color);
        movables.forEach( mov -> {
                    Point scaledPoint = new Point(
                            (int) Math.round(MINI_MAP_PERCENT * mov.getCenter().x / Game.BIG_UNIVERSE_SCALAR),
                            (int) Math.round(MINI_MAP_PERCENT *  mov.getCenter().y / Game.BIG_UNIVERSE_SCALAR)
                    );
                    g.fillOval(scaledPoint.x - 2, scaledPoint.y - 2, 4, 4);
                }

        );

    }//end method




}
