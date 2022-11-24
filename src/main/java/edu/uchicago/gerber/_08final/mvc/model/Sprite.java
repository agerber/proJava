package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.Game;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import lombok.Data;
import lombok.experimental.Tolerate;

//the lombok @Data gives us automatic getters and setters on all members
@Data
public abstract class Sprite implements Movable {
    //the center-point of this sprite
    private Point center;
    //this causes movement; change-in-x and change-in-y
    private double deltaX, deltaY;

    //every sprite has a team: friend, foe, floater, or debris.
    private Team team;
    //the radius of circumscribing circle
    private int radius;

    //orientation from 0-359
    private int orientation;
    //natural mortality (short-lived sprites only)
    private int expiry;
    //the color of this sprite
    private Color color;

    //some sprites spin, such as floaters and asteroids
    private int spin;

    //use for fade-in/fade-out
    private int fade;

    //these are Cartesian points used to draw the polygon.
    //once set, their values do not change. It's the job of the render() method to adjust for orientation and location.
    private Point[] cartesians;

    //constructor
    public Sprite() {

        //default sprite color
        setColor(Color.WHITE);
        //place the sprite at some random location in the frame at instantiation
        setCenter(new Point(Game.R.nextInt(Game.DIM.width),
                Game.R.nextInt(Game.DIM.height)));


    }


    @Override
    public void move() {

        //The following code block just keeps the sprite inside the bounds of the frame.
        //To ensure this behavior among all sprites in your game, make sure to call super.move() in extending classes
        // where you need to override the move() method.
        Point center = getCenter();

        //right-bounds reached
        if (center.x > Game.DIM.width) {
            setCenter(new Point(1, center.y));
        //left-bounds reached
        } else if (center.x < 0) {
            setCenter(new Point(Game.DIM.width - 1, center.y));
        //bottom-bounds reached
        } else if (center.y > Game.DIM.height) {
            setCenter(new Point(center.x, 1));
        //top-bounds reached
        } else if (center.y < 0) {
            setCenter(new Point(center.x, Game.DIM.height - 1));
        //in-bounds
        } else {
            double newXPos = center.x + getDeltaX();
            double newYPos = center.y + getDeltaY();
            setCenter(new Point((int) newXPos, (int) newYPos));
        }

        //expire (decrement expiry) on short-lived objects only
        //the default value of expiry is zero, so this block will only apply to expiring sprites
        if (getExpiry() > 0) expire();

        //if a sprite spins, adjust its orientation
        //the default value of spin is zero, therefore non-spinning objects will not call this block.
        if (getSpin() != 0) setOrientation(getOrientation() + getSpin());


    }

    private void expire() {

        //if a short-lived sprite has an expiry of one, it commits suicide by enqueuing itself (this) onto the
        //opsList with an operation of REMOVE
        if (getExpiry() == 1) {
            CommandCenter.getInstance().getOpsQueue().enqueue(this, GameOp.Action.REMOVE);
        }
        //and then decrements in all cases
        setExpiry(getExpiry() - 1);

    }




    protected int somePosNegValue(int seed) {
        int randomNumber = Game.R.nextInt(seed);
        if (randomNumber % 2 == 0)
            randomNumber = -randomNumber;
        return randomNumber;
    }

    @Override
    public boolean isProtected() {
        //by default, sprites are not protected
        return false;
    }

    @Override
    public void draw(Graphics g) {
        //set the native color of the sprite
        g.setColor(getColor());
        render(g);

    }

    public void draw(Graphics g, Color color) {
        //set custom color
        g.setColor(color);
        render(g);

    }

    private void render(Graphics g) {

        // to render this Sprite, we need to, 1: convert raw cartesians to raw polars, 2: adjust polars
        // for orientation of sprite. Convert back to cartesians 3: adjust for center-point (location).
        // and 4: pass the cartesian-x and cartesian-y coords as arrays, along with length, to drawPolygon().

        //convert raw cartesians to raw polars
        List<PolarPoint> polars = CommandCenter.cartesianToPolar(Arrays.asList(getCartesians()));

        //rotate raw polars given the orientation of the sprite. Then convert back to cartesians.
        Function<PolarPoint, Point> adjustForOrientation =
                pp -> new Point(
                        (int)  (pp.getR() * getRadius()
                                * Math.sin(Math.toRadians(getOrientation())
                                + pp.getTheta())),

                        (int)  (pp.getR() * getRadius()
                                * Math.cos(Math.toRadians(getOrientation())
                                + pp.getTheta())));

        // adjust for the location (center-point) of the sprite.
        // the reason we subtract the y-value has to do with how Java plots the vertical axis for
        // graphics (from top to bottom)
        Function<Point, Point> adjustForLocation =
                p -> new Point(
                         getCenter().x + p.x,
                         getCenter().y - p.y);



        g.drawPolygon(
                polars.stream()
                        .map(adjustForOrientation)
                        .map(adjustForLocation)
                        .map(pnt -> pnt.x)
                        .mapToInt(Integer::intValue)
                        .toArray(),

                polars.stream()
                        .map(adjustForOrientation)
                        .map(adjustForLocation)
                        .map(pnt -> pnt.y)
                        .mapToInt(Integer::intValue)
                        .toArray(),

                polars.size());

        //for debugging center-point. Feel free to remove these two lines.
        //#########################################
        g.setColor(Color.ORANGE);
        g.fillOval(getCenter().x - 1, getCenter().y - 1, 2, 2);
        //g.drawOval(getCenter().x - getRadius(), getCenter().y - getRadius(), getRadius() *2, getRadius() *2);
        //#########################################
    }


    public Point[] pointsListToArray(List<Point> listPoints) {
        return listPoints.stream()
                .toArray(Point[]::new);

    }


}
