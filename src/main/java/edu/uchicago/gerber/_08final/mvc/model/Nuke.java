package edu.uchicago.gerber._08final.mvc.model;


import java.awt.*;

public class Nuke extends Sprite{

    private final int MAX_RADIUS = 40;

    public Nuke(Falcon falcon) {
        setCenter(falcon.getCenter());
        setColor(Color.RED);
        setExpiry(MAX_RADIUS);
        setRadius(0);
        setTeam(Team.FRIEND);
    }


    @Override
    public void draw(Graphics g) {

        g.setColor(getColor());
        g.drawOval(getCenter().x, getCenter().y, getRadius(), getRadius());

    }




    @Override
    public void move() {
        super.move();
        if (getRadius() < MAX_RADIUS / 2)
            setRadius(getRadius() + 1);
        else
            setRadius(getRadius() -1);
        }



}
