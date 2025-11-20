package edu.uchicago.gerber._08final.mvc.model;


import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.awt.*;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.controller.GameOp;
import edu.uchicago.gerber._08final.mvc.controller.SoundLoader;
import edu.uchicago.gerber._08final.mvc.model.prime.PolarPoint;


public class Asteroid extends Sprite {

	//radius of a large asteroid
	private final int LARGE_RADIUS = 110;

	//size determines if the Asteroid is Large (0), Medium (1), or Small (2)
	public Asteroid(int size){

		//a size of zero is a big asteroid
		//a size of 1 or 2 is med or small asteroid respectively. See getSize() method.
		if (size == 0) setRadius(LARGE_RADIUS);
		else setRadius(LARGE_RADIUS/(size * 2));

		//Asteroid is FOE
		setTeam(Team.FOE);
		setColor(Color.WHITE);

		//the spin will be either plus or minus 0-9
		setSpin(somePosNegValue(10));
		//random delta-x
		setDeltaX(somePosNegValue(10));
		//random delta-y
		setDeltaY(somePosNegValue(10));

		setCartesians(generateVertices());

	}



	//overloaded constructor, so we can spawn smaller asteroids from an exploding one
	public Asteroid(Asteroid astExploded){
		//calls the other constructor: Asteroid(int size)
		this(astExploded.getSize() + 1);
		setCenter((Point)astExploded.getCenter().clone());
		int newSmallerSize = astExploded.getSize() + 1;
		//random delta-x : inertia + the smaller the asteroid, the faster its possible speed
		setDeltaX(astExploded.getDeltaX() / 1.5 + somePosNegValue( 5 + newSmallerSize * 2));
		//random delta-y : inertia + the smaller the asteroid, the faster its possible speed
		setDeltaY(astExploded.getDeltaY() / 1.5 + somePosNegValue( 5 + newSmallerSize * 2));

	}

	//converts the radius to integer representing the size of the Asteroid:
	//0 = large, 1 = medium, 2 = small
	public int getSize(){

		int rad = getRadius();
		return switch (rad) {
			case LARGE_RADIUS -> 0;
			case LARGE_RADIUS / 2 -> 1;
			case LARGE_RADIUS / 4 -> 2;
			default -> 0;
		};
	}



	private Point[] generateVertices() {


		final int VERTICES = 25 + Game.R.nextInt(7);    // 25–31 points
		final double MIN_RADIUS = 0.8;
		final double MAX_RADIUS = 1.0;

		Supplier<PolarPoint> polarPointSupplier = () -> {
			double r = MIN_RADIUS + (MAX_RADIUS - MIN_RADIUS) * Game.R.nextDouble();
			double theta = Game.R.nextDouble() * (2 * Math.PI);
			return new PolarPoint(r, theta);
		};

		Comparator<PolarPoint> byTheta = Comparator.comparingDouble(PolarPoint::getTheta);

		Function<PolarPoint, Point> toCartesian = pp -> {
			double radiusScale = 1000.0;
			int x = (int) (pp.getR() * radiusScale * Math.sin(pp.getTheta()));
			int y = (int) (pp.getR() * radiusScale * Math.cos(pp.getTheta()));
			return new Point(x, y);
		};

		return Stream.generate(polarPointSupplier)
				.limit(VERTICES)
				.sorted(byTheta)
				.map(toCartesian)
				.toArray(Point[]::new);
	}


	@Override
	public void draw(Graphics g) {
		renderVector(g);
	}

	@Override
	public void removeFromGame(LinkedList<Movable> list) {
		super.removeFromGame(list);
		spawnSmallerAsteroidsOrDebris();
		//give the user some points for destroying the asteroid
		CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + 10L * (getSize() + 1));

		//small (2) asteroids
		if (getSize() > 1)
			SoundLoader.playSound("pillow.wav");
		//else large (0) or medium (1) asteroids
		else
			SoundLoader.playSound("kapow.wav");

	}

	private void spawnSmallerAsteroidsOrDebris() {

		int size = getSize();
		//small (2) asteroids
		if (size > 1) {
			CommandCenter.getInstance().getOpsQueue().enqueue(new WhiteCloudDebris(this), GameOp.Action.ADD);
		}
		else {
			//for large (0) and medium (1) sized Asteroids only, spawn 2 or 3 smaller asteroids respectively
			//We can use the existing variable (size) to do this
			size += 2;
			while (size-- > 0) {
				CommandCenter.getInstance().getOpsQueue().enqueue(new Asteroid(this), GameOp.Action.ADD);
			}
		}

	}
}
