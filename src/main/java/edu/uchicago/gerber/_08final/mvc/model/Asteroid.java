package edu.uchicago.gerber._08final.mvc.model;


import java.util.Arrays;

import edu.uchicago.gerber._08final.mvc.controller.Game;

public class Asteroid extends Sprite {

	
	private int nSpin;
	
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

		//todo redundant
		assignRandomShape();
		
		//an nSize of zero is a big asteroid
		//a nSize of 1 or 2 is med or small asteroid respectively
		if (nSize == 0)
			setRadius(RAD);
		else
			setRadius(RAD/(nSize * 2));
		

	}

	private int somePosNegValue(int seed) {
		int randomNumber = Game.R.nextInt(seed);
		if (randomNumber % 2 == 0)
			randomNumber = -randomNumber;
		return randomNumber;
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

		assignRandomShape();
		
		//an nSize of zero is a big asteroid
		//a nSize of 1 or 2 is med or small asteroid respectively

		setRadius(RAD/(newSmallerSize * 2));
		setCenter(astExploded.getCenter());
		
		
		

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

	//todo use Lombok for getters/setters
	public int getSpin() {
		return this.nSpin;
	}
	

	public void setSpin(int nSpin) {
		this.nSpin = nSpin;
	}
	
	//this is for an asteroid only
	//todo use cartesian coordiantes and assign the pntCoords, and nothing else.
	  public void assignRandomShape ()
	  {
	    int nSide = Game.R.nextInt( 7 ) + 7;
	    int nSidesTemp = nSide;

	    int[] nSides = new int[nSide];
	    for ( int nC = 0; nC < nSides.length; nC++ )
	    {
	      int n = nC * 48 / nSides.length - 4 + Game.R.nextInt( 8 );
	      if ( n >= 48 || n < 0 )
	      {
	        n = 0;
	        nSidesTemp--;
	      }
	      nSides[nC] = n;
	    }

	    Arrays.sort( nSides );



	    double[]  dDegrees = new double[nSidesTemp];
	    for ( int nC = 0; nC <dDegrees.length; nC++ )
	    {
	    	dDegrees[nC] = nSides[nC] * Math.PI / 24 + Math.PI / 2;
	    }

	   //setDegrees( dDegrees);

		double[] dLengths = new double[dDegrees.length];
			for (int nC = 0; nC < dDegrees.length; nC++) {
				if(nC %3 == 0)
				    dLengths[nC] = 1 - Game.R.nextInt(40)/100.0;
				else
					dLengths[nC] = 1;
			}
		//setLengths(dLengths);
		polarToCartesian(dDegrees, dLengths);

	  }

}
