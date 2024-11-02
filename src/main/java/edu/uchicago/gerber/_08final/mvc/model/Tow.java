package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.SoundLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Tow extends Sprite {



    public Tow(Falcon falcon) {

        setTeam(Team.FRIEND);
        setColor(Color.RED);

        setExpiry(30);
        setRadius(20);


        //everything is relative to the falcon ship that fired the bullet
        setCenter((Point)falcon.getCenter().clone());

        //set the bullet orientation to the falcon (ship) orientation
        setOrientation(falcon.getOrientation());

        final double FIRE_POWER = 10.0;
        double vectorX =
                Math.cos(Math.toRadians(getOrientation())) * FIRE_POWER;
        double vectorY =
                Math.sin(Math.toRadians(getOrientation())) * FIRE_POWER;

        //fire force: falcon inertia + fire-vector
        setDeltaX(falcon.getDeltaX() + vectorX);
        setDeltaY(falcon.getDeltaY() + vectorY);

        //we have a reference to the falcon passed into the constructor. Let's create some kick-back.
        //fire kick-back on the falcon: inertia - fire-vector / some arbitrary divisor
       // final double KICK_BACK_DIVISOR = 36.0;
      //  falcon.setDeltaX(falcon.getDeltaX() - vectorX / KICK_BACK_DIVISOR);
       // falcon.setDeltaY(falcon.getDeltaY() - vectorY / KICK_BACK_DIVISOR);


        //define the points on a cartesian grid
        List<Point> listPoints = new ArrayList<>();
// Quadrant I
        listPoints.add(new Point(5, 50));
        listPoints.add(new Point(10, 45));
        listPoints.add(new Point(15, 40));
        listPoints.add(new Point(20, 35));
        listPoints.add(new Point(25, 30));
        listPoints.add(new Point(30, 25));
        listPoints.add(new Point(25, 15));
        listPoints.add(new Point(20, 10));
        listPoints.add(new Point(15, 5));
        listPoints.add(new Point(10, 0));

// Quadrant II
        listPoints.add(new Point(-5, 50));
        listPoints.add(new Point(-10, 45));
        listPoints.add(new Point(-15, 40));
        listPoints.add(new Point(-20, 35));
        listPoints.add(new Point(-25, 30));
        listPoints.add(new Point(-30, 25));
        listPoints.add(new Point(-25, 15));
        listPoints.add(new Point(-20, 10));
        listPoints.add(new Point(-15, 5));
        listPoints.add(new Point(-10, 0));

// Quadrant III
        listPoints.add(new Point(-10, -5));
        listPoints.add(new Point(-15, -10));
        listPoints.add(new Point(-20, -15));
        listPoints.add(new Point(-25, -20));
        listPoints.add(new Point(-30, -25));
        listPoints.add(new Point(-25, -35));
        listPoints.add(new Point(-20, -40));
        listPoints.add(new Point(-15, -45));
        listPoints.add(new Point(-10, -50));
        listPoints.add(new Point(-5, -55));

// Quadrant IV
        listPoints.add(new Point(10, -5));
        listPoints.add(new Point(15, -10));
        listPoints.add(new Point(20, -15));
        listPoints.add(new Point(25, -20));
        listPoints.add(new Point(30, -25));
        listPoints.add(new Point(25, -35));
        listPoints.add(new Point(20, -40));
        listPoints.add(new Point(15, -45));
        listPoints.add(new Point(10, -50));
        listPoints.add(new Point(5, -55));

// Points along the central axis (to add more radial points)
        listPoints.add(new Point(0, 60));
        listPoints.add(new Point(0, 55));
        listPoints.add(new Point(0, 50));
        listPoints.add(new Point(0, 45));
        listPoints.add(new Point(0, 40));
        listPoints.add(new Point(0, -40));
        listPoints.add(new Point(0, -45));
        listPoints.add(new Point(0, -50));
        listPoints.add(new Point(0, -55));
        listPoints.add(new Point(0, -60));


        setCartesians(listPoints.toArray(new Point[0]));




    }


    @Override
    public void draw(Graphics g) {
           renderVector(g);
    }

    @Override
    public void addToGame(LinkedList<Movable> list) {
        super.addToGame(list);
        SoundLoader.playSound("wall.wav");

    }
}
