package edu.uchicago.gerber._08final.mvc.model;

import lombok.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bullet extends Sprite {

    private final double FIRE_POWER = 35.0;


    public Bullet(Falcon fal) {

        super();
        setTeam(Team.FRIEND);


        //a bullet expires after 20 frames
        setExpiry(20);
        setRadius(6);


        //everything is relative to the falcon ship that fired the bullet
        setDeltaX(fal.getDeltaX() +
                Math.cos(Math.toRadians(fal.getOrientation())) * FIRE_POWER);
        setDeltaY(fal.getDeltaY() +
                Math.sin(Math.toRadians(fal.getOrientation())) * FIRE_POWER);
        setCenter(fal.getCenter());

        //set the bullet orientation to the falcon (ship) orientation
        setOrientation(fal.getOrientation());

        //make sure to setCartesianPoints last
        //defined the points on a cartesian grid
        List<Point> pntCs = new ArrayList<>();

        pntCs.add(new Point(0, 3)); //top point

        pntCs.add(new Point(1, -1));
        pntCs.add(new Point(0, -2));
        pntCs.add(new Point(-1, -1));
        setCartesians(pntCs);


    }

    @Override
    public void move() {
        super.move();
        expire();

    }

}
