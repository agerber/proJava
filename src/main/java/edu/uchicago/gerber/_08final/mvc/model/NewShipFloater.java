package edu.uchicago.gerber._08final.mvc.model;

import edu.uchicago.gerber._08final.mvc.controller.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewShipFloater extends Sprite {


	private int nSpin;

	public NewShipFloater() {

		super();
		setTeam(Team.FLOATER);
		List<Point> pntCs = new ArrayList<Point>();
		// top of ship
		pntCs.add(new Point(5, 5));
		pntCs.add(new Point(4,0));
		pntCs.add(new Point(5, -5));
		pntCs.add(new Point(0,-4));
		pntCs.add(new Point(-5, -5));
		pntCs.add(new Point(-4,0));
		pntCs.add(new Point(-5, 5));
		pntCs.add(new Point(0,4));

		setObjectPoints(pntCs);

		//todo redundant
		//assignPolarPoints(pntCs);

		setExpire(250);
		setRadius(50);
		setColor(Color.BLUE);


		int nX = Game.R.nextInt(10);
		int nY = Game.R.nextInt(10);
		int nS = Game.R.nextInt(5);
		
		//set random DeltaX
		if (nX % 2 == 0)
			setDeltaX(nX);
		else
			setDeltaX(-nX);

		//set rnadom DeltaY
		if (nY % 2 == 0)
			setDeltaX(nY);
		else
			setDeltaX(-nY);
		
		//set random spin
		if (nS % 2 == 0)
			setSpin(nS);
		else
			setSpin(-nS);

		//random point on the screen
		setCenter(new Point(Game.R.nextInt(Game.DIM.width),
				Game.R.nextInt(Game.DIM.height)));

		//random orientation 
		 setOrientation(Game.R.nextInt(360));

	}

	@Override
	public void move() {
		super.move();
		setOrientation(getOrientation() + getSpin());

		//todo put this  block in the Sprite class.
		if (getExpire() == 0)
			CommandCenter.getInstance().getOpsList().enqueue(this, CollisionOp.Operation.REMOVE);
		else
			setExpire(getExpire() - 1);


	}

	public int getSpin() {
		return this.nSpin;
	}

	public void setSpin(int nSpin) {
		this.nSpin = nSpin;
	}




	@Override
	public void draw(Graphics g) {
		super.draw(g);

		g.fillPolygon(
				convertStreamToArray(Arrays.stream(getObjectPoints()).map(pnt -> (int) pnt.getX())),
				convertStreamToArray(Arrays.stream(getObjectPoints()).map(pnt -> (int) pnt.getY())),
				getObjectPoints().length);

		//now draw a white border
		g.setColor(Color.WHITE);
		g.drawPolygon(
				convertStreamToArray(Arrays.stream(getObjectPoints()).map(pnt -> (int) pnt.getX())),
				convertStreamToArray(Arrays.stream(getObjectPoints()).map(pnt -> (int) pnt.getY())),
				getObjectPoints().length);
	}

}
