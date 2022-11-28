package edu.uchicago.gerber._08final.mvc.model;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SmallDebris extends Sprite{


    public SmallDebris(Asteroid explodingAsteroid) {

        //Debris means that this sprite does not interact with other teams.
        setTeam(Team.DEBRIS);

        //see readme.txt file in the resources/imgs/exp directory for how I created these assets
        Map<Integer, BufferedImage> rasterMap = new HashMap<>();
        rasterMap.put(0, loadGraphic("/imgs/exp/row-1-column-1.png") );
        rasterMap.put(1, loadGraphic("/imgs/exp/row-1-column-2.png") );
        rasterMap.put(2, loadGraphic("/imgs/exp/row-1-column-3.png") );
        rasterMap.put(3, loadGraphic("/imgs/exp/row-2-column-1.png") );
        rasterMap.put(4, loadGraphic("/imgs/exp/row-2-column-2.png") );
        rasterMap.put(5, loadGraphic("/imgs/exp/row-2-column-3.png") );
        rasterMap.put(6, loadGraphic("/imgs/exp/row-3-column-1.png") );
        rasterMap.put(7, loadGraphic("/imgs/exp/row-3-column-2.png") );
        rasterMap.put(8, loadGraphic("/imgs/exp/row-3-column-3.png") );

        setRasterMap(rasterMap);

        //expire it out after it has done its animation.
        setExpiry(rasterMap.size());

        //everything is relative to the exploding asteroid
        setSpin(explodingAsteroid.getSpin());
        setCenter(explodingAsteroid.getCenter());
        setDeltaX(explodingAsteroid.getDeltaX());
        setDeltaY(explodingAsteroid.getDeltaY());
        setRadius((int) (explodingAsteroid.getRadius() * 1.3));

    }

    //example of raster implementation of draw()
    @Override
    public void draw(Graphics g) {

        //we already have a counter with expiry; see move() method of Sprite
        int index = getRasterMap().size() - getExpiry() -1;
        renderRaster((Graphics2D) g, getRasterMap().get(index));


    }
}
