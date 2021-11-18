package edu.uchicago.gerber._08final.mvc.view;

import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.model.CommandCenter;
import edu.uchicago.gerber._08final.mvc.model.Falcon;
import edu.uchicago.gerber._08final.mvc.model.Movable;

import java.awt.*;
import java.util.Arrays;
import java.util.List;


public class GamePanel extends Panel {
	
	// ==============================================================
	// FIELDS 
	// ============================================================== 
	 
	// The following "off" vars are used for the off-screen double-bufferred image. 
	private Dimension dimOff;
	private Image imgOff;
	private Graphics grpOff;
	
	private GameFrame gmf;
	private Font fnt = new Font("SansSerif", Font.BOLD, 12);
	private Font fntBig = new Font("SansSerif", Font.BOLD + Font.ITALIC, 36);
	private FontMetrics fmt; 
	private int fontWidth;
	private int fontHeight;
	private String strDisplay = "";
	

	// ==============================================================
	// CONSTRUCTOR 
	// ==============================================================
	
	public GamePanel(Dimension dim){
	    gmf = new GameFrame();
		gmf.getContentPane().add(this);
		gmf.pack();
		initView();
		
		gmf.setSize(dim);
		gmf.setTitle("Game Base");
		gmf.setResizable(false);
		gmf.setVisible(true);
		this.setFocusable(true);
	}
	
	
	// ==============================================================
	// METHODS 
	// ==============================================================
	
	private void drawScore(Graphics g) {
		g.setColor(Color.white);
		g.setFont(fnt);
		if (CommandCenter.getInstance().getScore() != 0) {
			g.drawString("SCORE :  " + CommandCenter.getInstance().getScore(), fontWidth, fontHeight);
		} else {
			g.drawString("NO SCORE", fontWidth, fontHeight);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void update(Graphics g) {
		if (grpOff == null || Game.DIM.width != dimOff.width
				|| Game.DIM.height != dimOff.height) {
			dimOff = Game.DIM;
			imgOff = createImage(Game.DIM.width, Game.DIM.height);
			grpOff = imgOff.getGraphics();
		}
		// Fill in background with black.
		grpOff.setColor(Color.black);
		grpOff.fillRect(0, 0, Game.DIM.width, Game.DIM.height);

		drawScore(grpOff);
		
		if (!CommandCenter.getInstance().isPlaying()) {
			displayTextOnScreen();
		} else if (CommandCenter.getInstance().isPaused()) {
			strDisplay = "Game Paused";
			grpOff.drawString(strDisplay,
					(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4);
		}
		
		//playing and not paused!
		else {

			iterateMovables(grpOff,
			CommandCenter.getInstance().getMovDebris(),
			CommandCenter.getInstance().getMovFloaters(),
			CommandCenter.getInstance().getMovFoes(),
			CommandCenter.getInstance().getMovFriends());


			drawNumberShipsLeft(grpOff);


			if (CommandCenter.getInstance().isGameOver()) {
				CommandCenter.getInstance().setPlaying(false);
			}
		}

		//draw the double-Buffered Image to the graphics context of the panel
		g.drawImage(imgOff, 0, 0, this);
	} 


	
	//for each movable array, process it.
	private void iterateMovables(Graphics g, List<Movable>... arrayOfListMovables){
		
		for (List<Movable> movMovs : arrayOfListMovables) {
			for (Movable mov : movMovs) {

				mov.move();
				mov.draw(g);


			}
		}
		
	}


	private void drawNumberShipsLeft(Graphics g){
		int numFalcons = CommandCenter.getInstance().getNumFalcons();
		while (numFalcons > 0){
			drawOneShipLeft(g, numFalcons--);
		}
	}

	// Draw the number of falcons left on the bottom-right of the screen. Upside-down, but ok.
	private void drawOneShipLeft(Graphics g, int offSet) {
		Falcon falcon = CommandCenter.getInstance().getFalcon();

		g.setColor(Color.white);

		g.drawPolygon(
					Arrays.stream(falcon.getCartesianPoints())
							.map(pnt -> pnt.x + Game.DIM.width - (20 * offSet))
							.mapToInt(Integer::intValue)
							.toArray(),

					Arrays.stream(falcon.getCartesianPoints())
							.map(pnt -> pnt.y + Game.DIM.height - 40)
							.mapToInt(Integer::intValue)
							.toArray(),

					falcon.getCartesianPoints().length);



	}
	
	private void initView() {
		Graphics g = getGraphics();			// get the graphics context for the panel
		g.setFont(fnt);						// take care of some simple font stuff
		fmt = g.getFontMetrics();
		fontWidth = fmt.getMaxAdvance();
		fontHeight = fmt.getHeight();
		g.setFont(fntBig);					// set font info
	}
	
	// This method draws some text to the middle of the screen before/after a game
	private void displayTextOnScreen() {

		strDisplay = "GAME OVER";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4);

		strDisplay = "use the arrow keys to turn and thrust";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ fontHeight + 40);

		strDisplay = "use the space bar to fire";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ fontHeight + 80);

		strDisplay = "'S' to Start";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ fontHeight + 120);

		strDisplay = "'P' to Pause";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ fontHeight + 160);

		strDisplay = "'Q' to Quit";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ fontHeight + 200);
		strDisplay = "left pinkie on 'A' for Shield";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ fontHeight + 240);

		strDisplay = "'Numeric-Enter' for Hyperspace";
		grpOff.drawString(strDisplay,
				(Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
						+ fontHeight + 280);
	}
	

}