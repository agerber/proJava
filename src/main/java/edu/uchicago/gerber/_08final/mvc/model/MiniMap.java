package edu.uchicago.gerber._08final.mvc.model;



import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;

import java.awt.*;
import java.util.LinkedList;

/**
 * Inspired by Michael Vasiliou's Sinistar, winner of Java game contest 2016.
 */
public class MiniMap extends Sprite {
     //size of mini-map as percentage of screen
    private static final double MINI_MAP_PERCENT = 0.23;

    public MiniMap() {
        setTeam(Team.DEBRIS);
        setCenter(new Point(0,0));
    }

    public void move() {}

    public void draw(final Graphics g) {

        if (CommandCenter.getInstance().getUniverse() == CommandCenter.Universe.SMALL) return;


        int width = (int) Math.round(MINI_MAP_PERCENT * Game.DIM.width);
        int height = (int) Math.round(MINI_MAP_PERCENT * Game.DIM.height);

        //if BIG - show entire universe.
        if (CommandCenter.getInstance().getUniverse() == CommandCenter.Universe.BIG) {

            //gray bounding box (entire universe)
            g.setColor(Color.DARK_GRAY);
            g.drawRect(
                    0,
                    1, //adjust one pixel down
                    width,
                    height
            );
        } //end big

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

        //draw movables on mini-map
        drawRadarBlips(g, Color.darkGray, CommandCenter.getInstance().getMovDebris());
        drawRadarBlips(g, Color.WHITE, CommandCenter.getInstance().getMovFoes());
        drawRadarBlips(g, Color.CYAN, CommandCenter.getInstance().getMovFloaters());
        drawRadarBlips(g, Color.ORANGE, CommandCenter.getInstance().getMovFriends());



    }

    private void drawRadarBlips(final Graphics g, Color color, LinkedList<Movable> movables){

        g.setColor(color);
        movables.forEach( mov -> {
                    //transform the mov center-point so that when drawn, it fits within the mini-map
                    Point scaledPoint = new Point(
                            (int) Math.round(MINI_MAP_PERCENT * mov.getCenter().x / Game.BIG_UNIVERSE_SCALAR),
                            (int) Math.round(MINI_MAP_PERCENT *  mov.getCenter().y / Game.BIG_UNIVERSE_SCALAR)
                    );
                    //draw an oval (circle) with bounding box 4x4
                    g.fillOval(scaledPoint.x - 2, scaledPoint.y - 2, 4, 4);
                }

        );

    }//end method




}
