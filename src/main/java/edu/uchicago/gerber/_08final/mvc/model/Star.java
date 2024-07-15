package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import lombok.Data;

import java.awt.*;
import java.util.LinkedList;

//Sprite has a lot of bloat that we don't need to simply render a star field.
//This class demonstrates how we can use the Movable interface without extending Sprite.
@Data
public class Star implements Movable{

    private Point center;
    private Color color;

    public Star() {
        //center is some random point in the game space
        center = new Point(Game.R.nextInt(Game.DIM.width), Game.R.nextInt(Game.DIM.height));
        int bright = Game.R.nextInt(226); //Stars are muted at max brightness of 225 out of 255
        color = new Color(bright, bright, bright); //some grey value
    }

    //The following methods are contract methods from Movable. We need all of them to satisfy the contract.
    @Override
    public void draw(Graphics g) {

        g.setColor(color);
        g.drawOval(center.x, center.y, getRadius(), getRadius());

    }

    @Override
    public Point getCenter() {
        return center;
    }

    @Override
    public int getRadius() {
        return 1;
    }

    @Override
    public Team getTeam() {
        return Team.DEBRIS;
    }




    @Override
    public void move() {

        //if falcon position is NOT fixed return
        if (!CommandCenter.getInstance().isFalconPositionFixed()) return;

            //right-bounds reached
        if (center.x > Game.DIM.width) {
            setCenter(new Point(1, center.y));
            //left-bounds reached
        } else if (center.x < 0) {
            setCenter(new Point(Game.DIM.width - 1 , center.y));
            //bottom-bounds reached
        } else if (center.y > Game.DIM.height) {
            setCenter(new Point(center.x, 1));
            //top-bounds reached
        } else if (center.y < 0) {
            setCenter(new Point(center.x, Game.DIM.height - 1));
            //in-bounds
        } else {
            //move star in opposite direction of falcon.
            double newXPos = center.x - CommandCenter.getInstance().getFalcon().getDeltaX();
            double newYPos = center.y - CommandCenter.getInstance().getFalcon().getDeltaY();
            setCenter(new Point((int) Math.round(newXPos), (int) Math.round(newYPos)));
        }



    }


    @Override
    public void addToGame(LinkedList<Movable> list) {
        list.add(this);
    }

    @Override
    public void removeFromGame(LinkedList<Movable> list) {
       list.remove(this);
    }



}
