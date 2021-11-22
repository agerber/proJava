package edu.uchicago.gerber._08final.mvc.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.uchicago.gerber._08final.mvc.controller.Game;


public class Asteroid extends Sprite {

	//radius of a large asteroid
	private final int LARGE_RADIUS = 100;
	
	//size determines if the Asteroid is Large (0), Medium (1), or Small (2)
	//when you explode a Large asteroid, you should spawn 2 or 3 medium asteroids
	//same for medium asteroid, you should spawn small asteroids
	//small asteroids get blasted into debris, but do not spawn anything
	public Asteroid(int size){
		
		//call Sprite constructor
		super();

		setTeam(Team.FOE);

		//the spin will be either plus or minus 0-9
		setSpin(somePosNegValue(10));

		//random delta-x
		setDeltaX(somePosNegValue(10));

		//random delta-y
		setDeltaY(somePosNegValue(10));


		
		//an size of zero is a big asteroid
		//a size of 1 or 2 is med or small asteroid respectively
		if (size == 0)
			setRadius(LARGE_RADIUS);
		else
			setRadius(LARGE_RADIUS/(size * 2));


		//this method is in place of setting cartesian points
		assignRandomShape();

	}



	//overloaded so we can spawn smaller asteroids from an exploding one
	public Asteroid(Asteroid astExploded){
	

		//call Sprite constructor
		super();
		setTeam(Team.FOE);
		//the larger the number, the smaller the asteroid radius. See getSize() method below
		int  newSmallerSize =	astExploded.getSize() + 1;

		//the spin will be either plus or minus 0-9
		setSpin(somePosNegValue(10));

		//random delta-x - the smaller the asteroid the faster its possible speed
		setDeltaX(somePosNegValue(10 + newSmallerSize * 2));

		//random delta-y - the smaller the asteroid the faster its possible speed
		setDeltaY(somePosNegValue(10 + newSmallerSize * 2));


		setRadius(LARGE_RADIUS/(newSmallerSize * 2));
		setCenter(astExploded.getCenter());
		assignRandomShape();
		
		
		

	}

	public int getSize(){

		switch (getRadius()) {
			case 100:
				return 0;
			case 50:
				return 1;
			case 25:
				return 2;
			default:
				return 0;
		}
	}


	@Override
	public void move(){
		super.move();
		
		//an asteroid spins, so you need to adjust the orientation at each move()
		setOrientation(getOrientation() + getSpin());
		
	}


	  public void assignRandomShape (){

		  //6.283 is the max radians
		  final int MAX_RADIANS_X1000 =6283;

		  int sides = Game.R.nextInt( 7 ) + 17;
		  PolarPoint[] polarPairs = new PolarPoint[sides];
		  for ( int nC = 0; nC < polarPairs.length; nC++ ){
			  double r = (800 + Game.R.nextInt(200)) / 1000.0;
			  double theta = Game.R.nextInt(MAX_RADIANS_X1000) / 1000.0;
			  polarPairs[nC] = new PolarPoint(r,theta);
		  }

		 setCartesians(polarToCartesian(
				 Arrays.stream(polarPairs)
				 .sorted(new Comparator<PolarPoint>() {
					 @Override
					 public int compare(PolarPoint p1, PolarPoint p2) {
						 return  p1.getTheta().compareTo(p2.getTheta());
					 }
				 })
				 .collect(Collectors.toList()))
		 );

	  }

}
