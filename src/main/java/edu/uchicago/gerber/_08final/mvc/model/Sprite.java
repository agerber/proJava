package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.Game;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.experimental.Tolerate;

//the lombok @Data gives us automatic getters and setters on all members
@Data
public abstract class Sprite implements Movable {
	//the center-point of this sprite
	private Point center;
	//this causes movement; change in x and change in y
	private double deltaX, deltaY;

	//every sprite has a team: friend, foe, floater, or debris.
	private Team team;
	//the radius of circumscribing circle
	private int radius;

	//orientation from 0-359
	private int orientation;
	private int expiry; //natural mortality (short-living objects)
	//the color of this sprite
	private Color color;

	//some sprites spin, such as floaters and asteroids
	private int spin;

	//use for fade-in/fade-out
	private int fade;

	//these are Cartesian points used to draw the polygon.
	private Point[] cartesians;

	protected void expire(){
		if (getExpiry() == 0)
			CommandCenter.getInstance().getOpsList().enqueue(this, CollisionOp.Operation.REMOVE);
		else
			setExpiry(getExpiry() - 1);
	}

	@Override
	public void move() {

		Point pnt = getCenter();
		double newXPos = pnt.x + getDeltaX();
		double newYPos = pnt.y + getDeltaY();
		
		//the following code block just keeps the sprite inside the bounds of the frame
		//to ensure this behavior among all sprites in your game, make sure to call super.maove() in extending classes.
		if (pnt.x > Game.DIM.width) {
			setCenter(new Point(1, pnt.y));

		} else if (pnt.x < 0) {
			setCenter(new Point(Game.DIM.width - 1, pnt.y));
		} else if (pnt.y > Game.DIM.height) {
			setCenter(new Point(pnt.x, 1));

		} else if (pnt.y < 0) {
			setCenter(new Point(pnt.x, Game.DIM.height - 1));
		} else {

			setCenter(new Point((int) newXPos, (int) newYPos));
		}

	}

	public Sprite() {

		//default sprite color
		setColor(Color.white);
		//place the sprite at some random location in the frame at instantiation
		setCenter(new Point(Game.R.nextInt(Game.DIM.width),
				Game.R.nextInt(Game.DIM.height)));


	}

	protected double hypotFunction(double dX, double dY) {
		return Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
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

	//certain Sprites, such as Asteroid use this
	protected Point[] polarToCartesian(List<PolarPoint> pairs) {

		Function<PolarPoint, Point> polarToCartFunction = pair ->  new Point(
				(int) (getCenter().x + pair.getR() * getRadius() * 100
						* Math.sin(Math.toRadians(getOrientation())
						+ pair.getTheta())),
				(int) (getCenter().y - pair.getR() * getRadius() * 100
						* Math.cos(Math.toRadians(getOrientation())
						+ pair.getTheta())));

		return pairs.stream()
				.map(polarToCartFunction)
				.toArray(Point[]::new);

	}

	protected List<PolarPoint> cartesianToPolar(List<Point> pntCartesians){

		//determine the largest hypotenuse
		double largestHypotenuse = 0;
		for (Point pnt : pntCartesians)
			if (hypotFunction(pnt.x, pnt.y) > largestHypotenuse)
				largestHypotenuse = hypotFunction(pnt.x, pnt.y);


		//r is relative to the largestHypotenuse
		BiFunction<Point, Double, PolarPoint> pointDoublePairBiFunction = (pnt, dub) -> new PolarPoint(
				hypotFunction(pnt.x, pnt.y) / dub, //this is r from PolarPoint(r,theta)
				Math.toDegrees(Math.atan2(pnt.y, pnt.x)) * Math.PI / 180 //this is theta from PolarPoint(r,theta)
		);

		//we must make hypotenuse final to pass into a stream.
		final double hyp = largestHypotenuse;


		return pntCartesians.stream()
		     .map(pnt -> pointDoublePairBiFunction.apply(pnt, hyp))
			 .collect(Collectors.toList());

	}


	public void draw(Graphics g, Color color) {
		//set custom color
		g.setColor(color);
		render(g);

	}

	@Override
    public void draw(Graphics g) {
		//set the native color of the sprite
        g.setColor(getColor());
		render(g);

	}

	private void render(Graphics g) {

		//to render this Sprite, we need to, 1: convert cartesians to polars, 2: adjust the polar coords
		// by adjusting for both the center and orientation of sprite. 3: convert back to cartesians.
		List<PolarPoint> polars = cartesianToPolar(Arrays.asList(getCartesians()));

		Function<PolarPoint,Point> adjustPointFunction =
				pp -> new Point(
				(int) (getCenter().x + pp.getR() * getRadius()
						* Math.sin(Math.toRadians(getOrientation())
						+ pp.getTheta())),

				(int) (getCenter().y - pp.getR() * getRadius()
						* Math.cos(Math.toRadians(getOrientation())
						+ pp.getTheta())));


		g.drawPolygon(
				polars.stream()
						.map(adjustPointFunction)
						.map(pnt -> pnt.x)
						.mapToInt(Integer::intValue)
						.toArray(),

				polars.stream()
						.map(adjustPointFunction)
						.map(pnt -> pnt.y)
						.mapToInt(Integer::intValue)
						.toArray(),

				getCartesians().length);

		//for debugging center-point. Feel free to remove these two lines.
		//#########################################
		g.setColor(Color.ORANGE);
		g.fillOval(getCenter().x -1, getCenter().y -1, 2,2);
		//g.drawOval(getCenter().x - getRadius(), getCenter().y - getRadius(), getRadius() *2, getRadius() *2);
		//#########################################
	}


	//in order to overload a lombok'ed method, we need to use the @Tolerate annotation
	@Tolerate
	public void setCartesians(List<Point> pntPs) {
		setCartesians(pntPs.stream()
				.toArray(Point[]::new));

	}


}
