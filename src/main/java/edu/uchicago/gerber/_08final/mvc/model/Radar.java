package edu.uchicago.gerber._08final.mvc.model;



import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;

import java.awt.*;


/**
 * Inspired by Michael Vasiliou's Sinistar, winner of Java game contest 2016.
 */
public class Radar extends Sprite {


    //size of mini-map as percentage of screen (game dimension)
    private static final double RADAR_PERCENT = 0.31;
    private static final Color PUMPKIN = new Color(200, 100, 50);
    private static final Color LIGHT_GRAY = new Color(200, 200, 200);

    //a default no-arg constructor is automatically provided by Java
    //public Radar() {}

    //override and do nothing; the radar does not move.
    @Override
    public void move() {}

    @Override
    public void draw(Graphics g) {

        //controlled by the A-key
        if (!CommandCenter.getInstance().isRadarToggle()) return;

        int radarWidth = (int) Math.round(RADAR_PERCENT * Game.DIM.width );
        int radarHeight = (int) Math.round(RADAR_PERCENT * Game.DIM.height );

        //black fill and gray bounding box (entire universe)
        g.setColor(Color.BLACK);
        g.fillRect(
                0,
                0,
                radarWidth,
                radarHeight
        );
        g.setColor(Color.DARK_GRAY);
        g.drawRect(
                0,
                0,
                radarWidth,
                radarHeight
        );


        //draw the view-portal box
        g.setColor(Color.DARK_GRAY);
        int miniViewPortWidth = radarWidth / CommandCenter.getInstance().getUniDim().width;
        int miniViewPortHeight = radarHeight / CommandCenter.getInstance().getUniDim().height;
        g.drawRect(
                0 ,
                0,
                miniViewPortWidth,
                miniViewPortHeight

        );


        //draw debris radar-blips.
        CommandCenter.getInstance().getMovDebris().forEach( mov -> {
                    g.setColor(Color.DARK_GRAY);
                    Point translatedPoint = translatePoint(mov.getCenter());
                    g.fillOval(translatedPoint.x - 1, translatedPoint.y - 1, 2, 2);
                }
        );


        //draw foe (asteroids) radar-blips
        CommandCenter.getInstance().getMovFoes().forEach( mov -> {
                    if (!(mov instanceof  Asteroid)) return;
                    Asteroid asteroid = (Asteroid) mov;
                    g.setColor(LIGHT_GRAY);
                    Point translatedPoint = translatePoint(asteroid.getCenter());
                    switch (asteroid.getSize()){
                        //large
                        case 0:
                            g.fillOval(translatedPoint.x - 3, translatedPoint.y - 3, 6, 6);
                            break;
                        //med
                        case 1:
                            g.drawOval(translatedPoint.x - 2, translatedPoint.y - 2, 4, 4);
                            break;
                        //small
                        case 2:
                        default:
                            g.drawOval(translatedPoint.x - 1, translatedPoint.y - 1, 2, 2);
                    }
                }
        );


        //draw floater radar-blips
        CommandCenter.getInstance().getMovFloaters().forEach( mov -> {
                    g.setColor(mov instanceof NukeFloater ? Color.YELLOW : Color.CYAN);
                    Point translatedPoint = translatePoint(mov.getCenter());
                    g.fillRect(translatedPoint.x - 2, translatedPoint.y - 2, 4, 4);
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
                        color = PUMPKIN;
                    g.setColor(color);
                    Point translatedPoint = translatePoint(mov.getCenter());
                    g.fillOval(translatedPoint.x - 2, translatedPoint.y - 2, 4, 4);
                }
        );


    } //end draw

    //this function takes a center-point of a movable and scales it to display the blip on the radar.
    //Since Java's draw origin (0,0) is at the top-left, points will translate up and left.
    private Point translatePoint(Point point){
        Dimension dimension = CommandCenter.getInstance().getUniDim();
        return new Point(
                (int) Math.round( RADAR_PERCENT * point.x / dimension.width ),
                (int) Math.round( RADAR_PERCENT * point.y / dimension.height )
        );
    }
} //end class
