package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NewShipFloater extends Sprite {


	public NewShipFloater() {

		super();
		setTeam(Team.FLOATER);

		setExpiry(250);
		setRadius(50);
		setColor(Color.BLUE);

		//set random DeltaX
		setDeltaX(somePosNegValue(10));

		//set rnadom DeltaY
		setDeltaY(somePosNegValue(10));
		
		//set random spin
		setSpin(somePosNegValue(10));

		//random orientation 
		setOrientation(Game.R.nextInt(360));

		//always set cartesian points last
		List<Point> pntCs = new ArrayList<>();
		pntCs.add(new Point(5, 5));
		pntCs.add(new Point(4,0));
		pntCs.add(new Point(5, -5));
		pntCs.add(new Point(0,-4));
		pntCs.add(new Point(-5, -5));
		pntCs.add(new Point(-4,0));
		pntCs.add(new Point(-5, 5));
		pntCs.add(new Point(0,4));

		setCartesians(pntCs);
	}

	@Override
	public void move() {
		super.move();
		//a newShipFloater spins
		setOrientation(getOrientation() + getSpin());
		//and it also expires
		expire();

	}


}
