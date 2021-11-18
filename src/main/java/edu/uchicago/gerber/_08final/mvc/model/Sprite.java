package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.Game;
import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public abstract class Sprite implements Movable {
	//the center-point of this sprite
	private Point pntCenter;
	//this causes movement; change in x and change in y
	private double deltaX, deltaY;

	//every sprite has a team: friend, foe, floater, or debris.
	private Team team;

	//the radius of circumscribing circle
	private int rad;

	private int orientation;
	private int expiry; //natural mortality (short-living objects)
	//the color of this sprite
	private Color col;

	//some sprites spin, such as floaters and asteroids
	private int spin;

	//use for fade-in/fade-out
	private int fade;

	//these are used to draw the polygon. You don't usually need to interface with these
	private Point[] pntCoords; //an array of points used to draw polygon


	@Override
	public Team getTeam() {
		//default
	  return team;

	}

	public void setTeam(Team team){
		this.team = team;
	}

	public int getSpin() {
		return this.spin;
	}

	public void setSpin(int spin) {
		this.spin = spin;
	}

	protected void expire(){
		if (getExpire() == 0)
			CommandCenter.getInstance().getOpsList().enqueue(this, CollisionOp.Operation.REMOVE);
		else
			setExpire(getExpire() - 1);
	}

	public void move() {

		Point pnt = getCenter();
		double newXPos = pnt.x + getDeltaX();
		double newYPos = pnt.y + getDeltaY();
		
		//the following code block just keeps the sprite inside the bounds of the frame
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

	public void setExpire(int n) {
		expiry = n;

	}

	public Color getColor() {
		return col;
	}

	public void setColor(Color col) {
		this.col = col;

	}

	public int points() {
		//default is zero
		return 0;
	}

	public int getExpire() {
		return expiry;
	}

	public int getOrientation() {
		return orientation;
	}

	public void setOrientation(int n) {
		orientation = n;
	}

	public void setDeltaX(double dSet) {
		deltaX = dSet;
	}

	public void setDeltaY(double dSet) {
		deltaY = dSet;
	}

	public double getDeltaY() {
		return deltaY;
	}

	public double getDeltaX() {
		return deltaX;
	}

	public int getRadius() {
		return rad;
	}

	public void setRadius(int n) {
		rad = n;

	}


	public Point getCenter() {
		return pntCenter;
	}

	public void setCenter(Point pntParam) {
		pntCenter = pntParam;
	}


	protected double hypot(double dX, double dY) {
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
	protected Point[] polarToCartesian(List<Pair<Double,Double>> pairs) {

		int nC = 0;
		Point[] pnts = new Point[pairs.size()];
		for (Pair<Double, Double> pair : pairs) {
			if (nC % 2 != 0) //odd
			{
				pnts[nC] = new Point((int) (getCenter().x + pair.getValue() * getRadius() * 100
						* Math.sin(Math.toRadians(getOrientation())
						+ pair.getKey())),

						(int) (getCenter().y - pair.getValue() * getRadius() * 100
						* Math.cos(Math.toRadians(getOrientation())
						+ pair.getKey())));

			}
			//even
			else {
				pnts[nC] = new Point(
						(int) (getCenter().x + pair.getValue() * getRadius() * 100

						* Math.sin(Math.toRadians(getOrientation())
						+ pair.getKey())),


						(int) (getCenter().y - pair.getValue() * getRadius() * 100

								* Math.cos(Math.toRadians(getOrientation())
								+ pair.getKey())));

			} //end even/odd else
			nC++;
		} //end for loop

	return pnts;
			

	}


	protected List<Pair<Double,Double>> convertToPolars(List<Point> pntPoints){
		List<Pair<Double,Double>> polars = new ArrayList<>();

		//determine the largest hypotenuse
		double hypotenuse = 0;
		for (Point pnt : pntPoints)
			if (hypot(pnt.x, pnt.y) > hypotenuse)
				hypotenuse = hypot(pnt.x, pnt.y);


		for (Point pnt : pntPoints) {
			double len;
			if (pnt.x == 0 && pnt.y > 0) {
				len = hypot(pnt.x, pnt.y) / hypotenuse;
			} else if (pnt.x < 0 && pnt.y > 0) {
				len = hypot(pnt.x, pnt.y) / hypotenuse;
			} else {
				len = hypot(pnt.x, pnt.y) / hypotenuse;
			}

			polars.add(new Pair(Math.toDegrees(Math.atan2(pnt.y, pnt.x)) * Math.PI / 180, len));

		}
		return polars;

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
		//to render this Sprite, we need to adjust the original cartesian coords by calculating the orientation.
		Point[] adjPoints = new Point[getObjectPoints().length];
		List<Pair<Double,Double>> polars = convertToPolars(Arrays.asList(getObjectPoints()));

		for (int nC = 0; nC < getObjectPoints().length; nC++) {
			if (nC % 2 != 0) //odd
			{
				adjPoints[nC] = new Point((int) (getCenter().x + polars.get(nC).getValue() * getRadius()
						* Math.sin(Math.toRadians(getOrientation())
						+ polars.get(nC).getKey())), (int) (getCenter().y - polars.get(nC).getValue() * getRadius()
						* Math.cos(Math.toRadians(getOrientation())
						+ polars.get(nC).getKey())));

			}
			//even
			else {
				adjPoints[nC] = new Point((int) (getCenter().x + polars.get(nC).getValue() * getRadius()

						* Math.sin(Math.toRadians(getOrientation())
						+ polars.get(nC).getKey())),
						(int) (getCenter().y - polars.get(nC).getValue() * getRadius()

								* Math.cos(Math.toRadians(getOrientation())
								+ polars.get(nC).getKey())));

			} //end even/odd else

		} //end for loop


		g.drawPolygon(
				Arrays.stream(adjPoints)
						.map(pnt -> pnt.x)
						.mapToInt(Integer::intValue)
						.toArray(),

				Arrays.stream(adjPoints)
						.map(pnt -> pnt.y)
						.mapToInt(Integer::intValue)
						.toArray(),

				getObjectPoints().length);

		//for debugging center-point. Feel free to remove these two lines.
		//#########################################
		g.setColor(Color.RED);
		g.fillOval(getCenter().x, getCenter().y, 3,3);
		//#########################################
	}


	public Point[] getObjectPoints() {
		return pntCoords;
	}
	
	public void setObjectPoints(Point[] pntPs) {
		 pntCoords = pntPs;
	}

	public void setObjectPoints(List<Point> pntPs) {

		pntCoords = pntPs.stream()
				.toArray(Point[]::new);
	}
	
	public void setObjectPoint(Point pnt, int nIndex) {
		 pntCoords[nIndex] = pnt;
	}

	public int getFadeValue() {
		return fade;
	}

	public void setFadeValue(int n) {
		fade = n;
	}

}
