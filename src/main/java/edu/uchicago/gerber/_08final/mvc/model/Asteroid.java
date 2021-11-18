package edu.uchicago.gerber._08final.mvc.model;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.uchicago.gerber._08final.mvc.controller.Game;
import javafx.util.Pair;

public class Asteroid extends Sprite {

	//radius of a large asteroid
	private final int RAD = 100;
	
	//nSize determines if the Asteroid is Large (0), Medium (1), or Small (2)
	//when you explode a Large asteroid, you should spawn 2 or 3 medium asteroids
	//same for medium asteroid, you should spawn small asteroids
	//small asteroids get blasted into debris
	public Asteroid(int nSize){
		
		//call Sprite constructor
		super();

		setTeam(Team.FOE);

		//the spin will be either plus or minus 0-9
		setSpin(somePosNegValue(10));

		//random delta-x
		setDeltaX(somePosNegValue(10));

		//random delta-y
		setDeltaY(somePosNegValue(10));


		
		//an nSize of zero is a big asteroid
		//a nSize of 1 or 2 is med or small asteroid respectively
		if (nSize == 0)
			setRadius(RAD);
		else
			setRadius(RAD/(nSize * 2));


		assignRandomShape();

	}



	public Asteroid(Asteroid astExploded){
	

		//call Sprite constructor
		super();
		setTeam(Team.FOE);
		int  newSmallerSize =	astExploded.getSize() + 1;

		//the spin will be either plus or minus 0-9
		setSpin(somePosNegValue(10));

		//random delta-x - the smaller the asteroid the faster its possible speed
		setDeltaX(somePosNegValue(10 + newSmallerSize * 2));

		//random delta-y - the smaller the asteroid the faster its possible speed
		setDeltaY(somePosNegValue(10 + newSmallerSize * 2));


		setRadius(RAD/(newSmallerSize * 2));
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

		  int nSide = Game.R.nextInt( 7 ) + 17;
		  List<Pair<Double, Double>> pairs = new ArrayList<>();
		  for ( int nC = 0; nC < nSide; nC++ ){
		  	double theta = Game.R.nextInt(MAX_RADIANS_X1000) / 1000.0;
		  	double r = (800 + Game.R.nextInt(200)) / 1000.0;
			  pairs.add(new Pair<>(theta,r));
		  }

		 setCartesianPoints(polarToCartesian(

		 		pairs.stream()
				 .sorted(new Comparator<Pair<Double, Double>>() {
					 @Override
					 public int compare(Pair<Double, Double> p1, Pair<Double, Double> p2) {
						 return  p1.getKey().compareTo(p2.getKey());
					 }
				 })
				 .collect(Collectors.toList()))
		 );

	  }

}
