package edu.uchicago.gerber._08final.mvc.controller;


import java.awt.*;
import edu.uchicago.gerber._08final.mvc.model.*;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

//The CommandCenter is a singleton that manages the state of the game.
//the lombok @Data gives us automatic getters and setters on all members
@Data
public class CommandCenter {

	public enum Universe {
		SMALL_FREE_FLY,
		SMALL,
		BIG,
		HORIZONTAL

	}


	public Universe universe = Universe.BIG;


	private  int numFalcons;
	private  int level;
	private  long score;
	private  boolean paused;
	private  boolean themeMusic;
	//this value is used to count the number of frames (full animation cycles) in the game
	private long frame;

	//the falcon is located in the movFriends list, but since we use this reference a lot, we keep track of it in a
	//separate reference. Use final to ensure that the falcon ref always points to the single falcon object on heap.
	//Lombok will not provide setter methods on final members

	private final Falcon falcon  = new Falcon();

	//miniDimHash contains meta-data dimension about the Universe.
	private Map<Universe, Dimension> miniDimHash = new HashMap<>();
	private final MiniMap miniMap = new MiniMap();

	//lists containing our movables subdivided by team
	private final LinkedList<Movable> movDebris = new LinkedList<>();
	private final LinkedList<Movable> movFriends = new LinkedList<>();
	private final LinkedList<Movable> movFoes = new LinkedList<>();
	private final LinkedList<Movable> movFloaters = new LinkedList<>();

	private final GameOpsQueue opsQueue = new GameOpsQueue();


	//singleton
	private static CommandCenter instance = null;

	// Constructor made private
	private CommandCenter() {}

    //this class maintains game state - make this a singleton.
	public static CommandCenter getInstance(){
		if (instance == null){
			instance = new CommandCenter();
		}
		return instance;
	}



	public void cycleUniverse() {
		//cycle universe among its vals
		switch (universe) {
			case SMALL_FREE_FLY:
				universe = Universe.SMALL;
				break;
			case SMALL:
				universe = Universe.BIG;
				break;
			case BIG:
				universe = Universe.HORIZONTAL;
				break;
			case HORIZONTAL:
				universe = Universe.SMALL_FREE_FLY;
				break;
		}
	}



	public void initGame(){
		clearAll();
		generateStarField();

		miniDimHash.put(Universe.SMALL_FREE_FLY, new Dimension(1,1));
		miniDimHash.put(Universe.SMALL, new Dimension(1,1));
		miniDimHash.put(Universe.BIG, new Dimension(2,2));
		miniDimHash.put(Universe.HORIZONTAL, new Dimension(2,1));

		setLevel(0);
		setScore(0);
		setPaused(false);
		//set to one greater than number of falcons lives in your game as decrementFalconNumAndSpawn() also decrements
		setNumFalcons(4);
		falcon.decrementFalconNumAndSpawn();
		opsQueue.enqueue(falcon, GameOp.Action.ADD);
		opsQueue.enqueue(miniMap, GameOp.Action.ADD);

		//if you like the theme to Dr. Who, uncomment these two lines to start with music playing; M toggle mute
//		setThemeMusic(true);
//		SoundLoader.playSound("dr-who_loop.wav");



	}

	private void generateStarField(){

		int count = 100;
		while (count-- > 0){
			opsQueue.enqueue(new Star(), GameOp.Action.ADD);
		}

	}



	public void incrementFrame(){
		//use of ternary expression to simplify the logic to one line
		frame = frame < Long.MAX_VALUE ? frame + 1 : 0;
	}

	private void clearAll(){
		movDebris.clear();
		movFriends.clear();
		movFoes.clear();
		movFloaters.clear();
	}

	public boolean isGameOver() {		//if the number of falcons is zero, then game over
		return numFalcons < 1;
	}

	public Dimension getUniDim(){
		return miniDimHash.get(universe);
	}

	public boolean isFalconPositionFixed(){
		return universe != Universe.SMALL_FREE_FLY;
	}






}
