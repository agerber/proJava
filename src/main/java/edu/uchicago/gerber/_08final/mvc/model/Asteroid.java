package edu.uchicago.gerber._08final.mvc.model;


import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.awt.*;

import edu.uchicago.gerber._08final.mvc.controller.Game;


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
		setCenter(astExploded.getCenter());
		int newSmallerSize = astExploded.getSize() + 1;
		//random delta-x : inertia + the smaller the asteroid, the faster its possible speed
		setDeltaX(astExploded.getDeltaX() / 1.5 + somePosNegValue( 5 + newSmallerSize * 2));
		//random delta-y : inertia + the smaller the asteroid, the faster its possible speed
		setDeltaY(astExploded.getDeltaY() / 1.5 + somePosNegValue( 5 + newSmallerSize * 2));

	}

	//converts the radius to integer representing the size of the Asteroid:
	//0 = large, 1 = medium, 2 = small
	public int getSize(){
		switch (getRadius()) {
			case LARGE_RADIUS:
				return 0;
			case LARGE_RADIUS / 2:
				return 1;
			case LARGE_RADIUS / 4:
				return 2;
			default:
				return 0;
		}
	}



	  private Point[] generateVertices(){

		  //6.283 is the max radians
		  final int MAX_RADIANS_X1000 =6283;
		  //When casting from double to int, we truncate and lose precision, so best to be generous with the
		  //precision factor as this will create a more random distribution of vertices. Precision is a proxy for
		  //radius in the absence of a predefined radius.
		  final double PRECISION = 100.0;

		  Supplier<PolarPoint> polarPointSupplier = () -> {
			  double r = (800 + Game.R.nextInt(200)) / 1000.0; //number between 0.8 and 0.999
			  double theta = Game.R.nextInt(MAX_RADIANS_X1000) / 1000.0; // number between 0 and 6.282
		  	  return new PolarPoint(r, theta);
		  };

		  Function<PolarPoint, Point> polarToCartesian =
				  pp -> new Point(
						  (int)  (pp.getR() * PRECISION * Math.sin(pp.getTheta())),
						  (int)  (pp.getR() * PRECISION * Math.cos(pp.getTheta())));

		 //random number of vertices
		 final int VERTICES = Game.R.nextInt(7) + 25;

		 return Stream.generate(polarPointSupplier)
				 //the supplier stream will never terminate unless we use a limit.
				 .limit(VERTICES)
				 //I used the 'new' keyword to generate the anon-inner class; you can convert to lambda.
				 //The polar-points must be sorted by theta, otherwise they will not render as asteroids, but
				 //rather as a bundle of jaggedy lines. Try removing the .sorted() call to see how they render.
				 .sorted(new Comparator<PolarPoint>() {
							@Override
							public int compare(PolarPoint pp1, PolarPoint pp2) {
								return  pp1.getTheta().compareTo(pp2.getTheta());
							}
						})
				 .map(polarToCartesian)
				 .toArray(Point[]::new);


	  }

	@Override
	public void draw(Graphics g) {
		renderVector(g);
	}



}
