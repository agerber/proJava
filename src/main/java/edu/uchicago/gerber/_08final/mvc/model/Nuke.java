package edu.uchicago.gerber._08final.mvc.model;


import java.awt.*;

public class Nuke extends Sprite{

    private final int MAX_COUNT = 40;
    private int count;

    public Nuke(Falcon falcon) {
        setCenter(falcon.getCenter());
        setColor(Color.RED);
        setExpiry(MAX_COUNT);
        setRadius(0);
        setTeam(Team.FRIEND);
    }


    @Override
    public void draw(Graphics g) {

        g.setColor(getColor());
        g.drawOval(getCenter().x -getRadius(), getCenter().y - getRadius(), getRadius() * 2, getRadius()* 2);

    }

    @Override
    public void move() {
        super.move();

        if (count++ <= MAX_COUNT / 2)
            setRadius(getRadius() + 16);
        else
            setRadius(getRadius() -16);
        }

}
