package edu.uchicago.gerber._08final.mvc.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Floater extends Sprite {

    public Floater() {

        setTeam(Team.FLOATER);

        setExpiry(250);
        setRadius(50);


        //set random DeltaX
        setDeltaX(somePosNegValue(10));

        //set random DeltaY
        setDeltaY(somePosNegValue(10));

        //set random spin
        setSpin(somePosNegValue(10));

        //cartesian points which define the shape of the polygon
        List<Point> listPoints = new ArrayList<>();
        listPoints.add(new Point(5, 5));
        listPoints.add(new Point(4,0));
        listPoints.add(new Point(5, -5));
        listPoints.add(new Point(0,-4));
        listPoints.add(new Point(-5, -5));
        listPoints.add(new Point(-4,0));
        listPoints.add(new Point(-5, 5));
        listPoints.add(new Point(0,4));

        setCartesians(CommandCenter.pointsListToArray(listPoints));

    }

    @Override
    public void draw(Graphics g) {
        renderVector(g);
    }

}
