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
        double vectorX =
                Math.cos(Math.toRadians(getOrientation())) * FIRE_POWER;
        double vectorY =
                Math.sin(Math.toRadians(getOrientation())) * FIRE_POWER;

        //fire force: falcon inertia + fire-vector
        setDeltaX(falcon.getDeltaX() + vectorX);
        setDeltaY(falcon.getDeltaY() + vectorY);

        //fire kick-back on the falcon: inertia - fire-vector / some arbitrary divisor
        final double KICK_BACK_DIVISOR = 36.0;
        falcon.setDeltaX(falcon.getDeltaX() - vectorX / KICK_BACK_DIVISOR);
        falcon.setDeltaY(falcon.getDeltaY() - vectorY / KICK_BACK_DIVISOR);


        //defined the points on a cartesian grid
        List<Point> listPoints = new ArrayList<>();
        listPoints.add(new Point(0, 3)); //top point
        listPoints.add(new Point(1, -1));
        listPoints.add(new Point(0, -2));
        listPoints.add(new Point(-1, -1));

        setCartesians(listPoints.toArray(new Point[0]));




    }


    @Override
    public void draw(Graphics g) {
           renderVector(g);
    }
}
