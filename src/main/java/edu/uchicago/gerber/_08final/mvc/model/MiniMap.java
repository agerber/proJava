package edu.uchicago.gerber._08final.mvc.model;



import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;

import java.awt.*;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Function;



/**
 * Inspired by Michael Vasiliou's Sinistar, winner of Java game contest 2016.
 */
public class MiniMap extends Sprite {
     //size of mini-map as percentage of screen
    private static final double MINI_MAP_PERCENT = 0.23;

    private Color pumpkin = new Color(200, 100, 50);

    public MiniMap() {
        setTeam(Team.DEBRIS);
        setCenter(new Point(0,0));
    }

    public void move() {}

    @Override
    public void draw(Graphics g) {

        if (CommandCenter.getInstance().getUniverse() == CommandCenter.Universe.SMALL) return;

        //scale the mini-map to some percent of game-dim
        int miniWidth = (int) Math.round(MINI_MAP_PERCENT * Game.DIM.width);
        int miniHeight = (int) Math.round(MINI_MAP_PERCENT * Game.DIM.height);

        //if BIG_CENTERED - show the entire big universe in mini-map.
        if (CommandCenter.getInstance().getUniverse() == CommandCenter.Universe.BIG_CENTERED) {

            //gray bounding box (entire universe)
            g.setColor(Color.DARK_GRAY);
            g.drawRect(
                    0,
                    0,
                    miniWidth,
                    miniHeight
            );
        } //end big

        //in the case of both SMALL_CENTERED AND BIG_CENTERED, show player's view of universe
        g.setColor(Color.DARK_GRAY);
        int miniViewPortWidth = miniWidth / Game.BIG_UNIVERSE_SCALAR;
        int miniViewPortHeight = miniHeight / Game.BIG_UNIVERSE_SCALAR;
        g.drawRect(
                0 ,
                0,
                miniViewPortWidth,
                miniViewPortHeight

        );

        //draw debris radar-blips
        CommandCenter.getInstance().getMovDebris().forEach( mov -> {
                    g.setColor(Color.darkGray);
                    Point scaledPoint = scalePoint(mov.getCenter());
                    g.fillOval(scaledPoint.x - 1, scaledPoint.y - 1, 2, 2);
                }
        );


        //draw foe (asteroids) radar-blips
        CommandCenter.getInstance().getMovFoes().forEach( mov -> {
                    g.setColor(Color.WHITE);
                    Point scaledPoint = scalePoint(mov.getCenter());
                    g.fillOval(scaledPoint.x - 2, scaledPoint.y - 2, 4, 4);
                }
        );


        //draw floater radar-blips
        CommandCenter.getInstance().getMovFloaters().forEach( mov -> {
                    g.setColor(mov instanceof NukeFloater ? Color.YELLOW : Color.CYAN);
                    Point scaledPoint = scalePoint(mov.getCenter());
                    g.fillRect(scaledPoint.x - 2, scaledPoint.y - 2, 4, 4);
                }
        );



        //draw friend radar-blips
        CommandCenter.getInstance().getMovFriends().forEach( mov -> {
                    Color color;
                    if (mov instanceof Falcon && CommandCenter.getInstance().getFalcon().getShield() > 0)
                        color = Color.CYAN;
                    else if (mov instanceof Nuke)
                        color = Color.YELLOW;
                    else
                        color = pumpkin;
                    g.setColor(color);
                    Point scaledPoint = scalePoint(mov.getCenter());
                    g.fillOval(scaledPoint.x - 2, scaledPoint.y - 2, 4, 4);
                }
        );


    }

    //this function takes a center-point of a movable and scales it to display the blip on the mini-map.
    //Since Java's draw origin (0,0) is at the top-left, points will translate up and left.
    private Point scalePoint(Point point){
        return new Point(
                (int) Math.round(MINI_MAP_PERCENT * point.x / Game.BIG_UNIVERSE_SCALAR),
                (int) Math.round(MINI_MAP_PERCENT * point.y / Game.BIG_UNIVERSE_SCALAR)
        );
    }




}
