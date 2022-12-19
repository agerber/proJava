package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Utils;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bullet extends Sprite {



    public Bullet(Falcon falcon) {



        setTeam(Team.FRIEND);
        setColor(Color.ORANGE);

        //a bullet expires after 20 frames.
        setExpiry(20);
        setRadius(6);


        //everything is relative to the falcon ship that fired the bullet
        setCenter(falcon.getCenter());

        //set the bullet orientation to the falcon (ship) orientation
        setOrientation(falcon.getOrientation());

        final double FIRE_POWER = 35.0;
        double vectorX = falcon.getDeltaX() +
                Math.cos(Math.toRadians(falcon.getOrientation())) * FIRE_POWER;
        double vectorY = falcon.getDeltaY() +
                Math.sin(Math.toRadians(falcon.getOrientation())) * FIRE_POWER;

        //fire vectors forces
        setDeltaX(vectorX);
        setDeltaY(vectorY);

        //fire kick-back on the falcon
        final double KICK_BACK_DIVISOR = 160.0;
        falcon.setDeltaX(falcon.getDeltaX() - vectorX / KICK_BACK_DIVISOR);
        falcon.setDeltaY(falcon.getDeltaY() - vectorY / KICK_BACK_DIVISOR);


        //defined the points on a cartesian grid
        List<Point> listPoints = new ArrayList<>();
        listPoints.add(new Point(0, 3)); //top point
        listPoints.add(new Point(1, -1));
        listPoints.add(new Point(0, -2));
        listPoints.add(new Point(-1, -1));

        setCartesians(Utils.pointsListToArray(listPoints));




    }


    @Override
    public void draw(Graphics g) {
           renderVector(g);
    }
}
