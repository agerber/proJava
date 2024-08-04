package edu.uchicago.gerber._08final.mvc.model;



import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;

import java.awt.*;


/**
 * Inspired by Michael Vasiliou's Sinistar, winner of Java game contest 2016.
 */
public class MiniMap extends Sprite {


    //size of mini-map as percentage of screen (game dimension)
    private final double MINI_MAP_PERCENT = 0.31;

    //used to adjust non-square universes. Set in draw()
    private AspectDim aspectDim;

    private final Color pumpkin = new Color(200, 100, 50);

    public MiniMap() {
        setTeam(Team.DEBRIS);
        setCenter(new Point(0,0));
    }

    @Override
    public void move() {}


    @Override
    public void draw(Graphics g) {

        //exclude ordinals 0 and 1 (the small universes)
        if (CommandCenter.getInstance().getUniverse().ordinal() < 2) return;

        //get the aspect-dimension which is used for those universes with differing widths and heights
        aspectDim = aspectAdjustedDimension(CommandCenter.getInstance().getUniDim());

        //scale to some percent of game-dim
        int miniWidth = (int) Math.round( MINI_MAP_PERCENT * Game.DIM.width * aspectDim.getWidth());
        int miniHeight = (int) Math.round(MINI_MAP_PERCENT * Game.DIM.height * aspectDim.getHeight());

        //gray bounding box (entire universe)
        g.setColor(Color.DARK_GRAY);
        g.drawRect(
                0,
                0,
                miniWidth,
                miniHeight
        );


        //draw the view-portal box
        g.setColor(Color.DARK_GRAY);
        int miniViewPortWidth = miniWidth / CommandCenter.getInstance().getUniDim().width;
        int miniViewPortHeight = miniHeight / CommandCenter.getInstance().getUniDim().height;
        g.drawRect(
                0 ,
                0,
                miniViewPortWidth,
                miniViewPortHeight

        );


        //draw debris radar-blips.
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
                (int) Math.round( MINI_MAP_PERCENT  * point.x / CommandCenter.getInstance().getUniDim().width * aspectDim.getWidth()),
                (int) Math.round( MINI_MAP_PERCENT  * point.y / CommandCenter.getInstance().getUniDim().height * aspectDim.getHeight())
        );
    }


    //the purpose of this method is to adjust the aspect of non-square universes
    private AspectDim aspectAdjustedDimension(Dimension universeDim){
        if (universeDim.width == universeDim.height){
            return new AspectDim(1.0, 1.0);
        }
        else if (universeDim.width > universeDim.height){
            double wMultiple = (double) universeDim.width / universeDim.height;
            return new AspectDim(wMultiple, 1.0).scale(0.5);
        }
        //universeDim.width < universeDim.height
        else {
            double hMultiple = (double) universeDim.height / universeDim.width;
            return new AspectDim(1.0, hMultiple).scale(0.5);
        }

    }



}
