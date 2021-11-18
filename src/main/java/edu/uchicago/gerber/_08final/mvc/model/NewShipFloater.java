package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NewShipFloater extends Sprite {


	public NewShipFloater() {

		super();
		setTeam(Team.FLOATER);



		setExpire(250);
		setRadius(50);
		setColor(Color.BLUE);

		//set random DeltaX
		setDeltaX(somePosNegValue(10));

		//set rnadom DeltaY
		setDeltaY(somePosNegValue(10));
		
		//set random spin
		setSpin(somePosNegValue(10));

		//random point on the screen
		setCenter(new Point(Game.R.nextInt(Game.DIM.width),
				Game.R.nextInt(Game.DIM.height)));

		//random orientation 
		setOrientation(Game.R.nextInt(360));

		//always set cartesean points last
		List<Point> pntCs = new ArrayList<Point>();
		pntCs.add(new Point(5, 5));
		pntCs.add(new Point(4,0));
		pntCs.add(new Point(5, -5));
		pntCs.add(new Point(0,-4));
		pntCs.add(new Point(-5, -5));
		pntCs.add(new Point(-4,0));
		pntCs.add(new Point(-5, 5));
		pntCs.add(new Point(0,4));

		setCartesianPoints(pntCs);
	}

	@Override
	public void move() {
		super.move();
		setOrientation(getOrientation() + getSpin());
		expire();



	}


	@Override
	public void draw(Graphics g) {
		super.draw(g);
	}

}
