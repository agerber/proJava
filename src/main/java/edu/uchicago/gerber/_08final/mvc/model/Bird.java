package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.controller.ImageLoader;
import edu.uchicago.gerber._08final.mvc.controller.SoundLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Bird extends Sprite {


    private int index = 0;
    private final static int SLOW_MO = 3;
    public Bird() {

        setTeam(Team.FOE);
       // setColor(Color.ORANGE);

        setExpiry(150);
        setRadius(120);


        setCenter(new Point(Game.R.nextInt(Game.DIM.width),Game.R.nextInt(Game.DIM.height)));

        //set the bullet orientation to the falcon (ship) orientation
        setOrientation(0);

        final double FIRE_POWER = 10.0;
        double vectorX =
                Math.cos(Math.toRadians(getOrientation())) * FIRE_POWER;
        double vectorY =
                Math.sin(Math.toRadians(getOrientation())) * FIRE_POWER;

        //fire force: falcon inertia + fire-vector
        setDeltaX(somePosNegValue(20) + vectorX);
        setDeltaY(somePosNegValue(20) + vectorY);


        Map<Integer, BufferedImage> rasterMap = new HashMap<>();
        //see readme.txt file in the resources/imgs/exp directory for how I created these assets
        rasterMap.put(0, ImageLoader.getImage("/imgs/bird/row-1-column-1.png"));
        rasterMap.put(1, ImageLoader.getImage("/imgs/bird/row-1-column-2.png"));
        rasterMap.put(2, ImageLoader.getImage("/imgs/bird/row-1-column-3.png"));
        rasterMap.put(3, ImageLoader.getImage("/imgs/bird/row-2-column-1.png"));
        rasterMap.put(4, ImageLoader.getImage("/imgs/bird/row-2-column-2.png"));
        rasterMap.put(5, ImageLoader.getImage("/imgs/bird/row-2-column-3.png"));
        rasterMap.put(6, ImageLoader.getImage("/imgs/bird/row-3-column-1.png"));
        rasterMap.put(7, ImageLoader.getImage("/imgs/bird/row-3-column-2.png"));
        rasterMap.put(8, ImageLoader.getImage("/imgs/bird/row-3-column-3.png"));

        setRasterMap(rasterMap);







    }


    @Override
    public void draw(Graphics g) {
        renderRaster((Graphics2D) g, getRasterMap().get(index));

        if (index == 8 && getExpiry() % SLOW_MO == 0){
            index = 0;
        } else {
            if (getExpiry() % SLOW_MO == 0) index++;
        }

    }

    @Override
    public void removeFromGame(LinkedList<Movable> list) {
        super.removeFromGame(list);
        SoundLoader.playSound("laser.wav");
    }


}
