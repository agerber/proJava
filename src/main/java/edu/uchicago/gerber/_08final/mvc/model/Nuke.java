package edu.uchicago.gerber._08final.mvc.model;


import java.awt.*;

public class Nuke extends Sprite{

    private final int EXPIRE = 60;
    private int count = 0;

    public Nuke(Falcon falcon) {
        setCenter(falcon.getCenter());
        setColor(Color.YELLOW);
        setExpiry(EXPIRE);
        setRadius(0);
        setTeam(Team.FRIEND);
        //the nuke is a cicle, so it has no orientation, but we need this to calculate vectors
        setOrientation(falcon.getOrientation());

        final double FIRE_POWER = 11.0;
        double vectorX =
                Math.cos(Math.toRadians(getOrientation())) * FIRE_POWER;
        double vectorY =
                Math.sin(Math.toRadians(getOrientation())) * FIRE_POWER;

        //fire force: falcon inertia + fire-vector
        setDeltaX(falcon.getDeltaX() + vectorX);
        setDeltaY(falcon.getDeltaY() + vectorY);

    }


    @Override
    public void draw(Graphics g) {

        g.setColor(getColor());
        g.drawOval(getCenter().x -getRadius(), getCenter().y - getRadius(), getRadius() * 2, getRadius()* 2);

    }

    //a nuke is invincible while it hasn't yet expired
    @Override
    public boolean isProtected() {
        return true;
    }

    @Override
    public void move() {
        super.move();
        if (getExpiry() % 20 == 0) count++;
        switch (count) {
            case 0:
            default:
                setRadius(2);
                break;
            case 1:
                setRadius(getRadius() + 16);
                break;
            case 2:
                setRadius(getRadius() - 16);
                break;


        }

    }


}