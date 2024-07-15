package edu.uchicago.gerber._08final.mvc.controller;



import edu.uchicago.gerber._08final.mvc.model.*;
import lombok.Data;

import java.util.LinkedList;

//The CommandCenter is a singleton that manages the state of the game.
//the lombok @Data gives us automatic getters and setters on all members
@Data
public class CommandCenter {


	public enum Universe {
		SMALL,
		SMALL_CENTERED,
		BIG_CENTERED

	}

	public Universe universe = Universe.SMALL;
	private  int numFalcons;
	private  int level;
	private  long score;
	private  boolean paused;
	private  boolean muted;
	//this value is used to count the number of frames (full animation cycles) in the game
	private long frame;

	//the falcon is located in the movFriends list, but since we use this reference a lot, we keep track of it in a
	//separate reference. Use final to ensure that the falcon ref always points to the single falcon object on heap.
	//Lombok will not provide setter methods on final members

	private final Falcon falcon  = new Falcon();
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

	public int getUniverseScalar() {
		return universe == Universe.BIG_CENTERED ? Game.BIG_UNIVERSE_SCALAR : 1;
	}

	public void cycleUniverse() {
		//tri-cycle universe among its vals
		switch (universe) {
			case SMALL:
				universe = Universe.SMALL_CENTERED;
				break;
			case SMALL_CENTERED:
				universe = Universe.BIG_CENTERED;
				break;
			case BIG_CENTERED:
				universe = Universe.SMALL;
				break;
		}
	}

	public boolean isFalconCentered(){
		return CommandCenter.getInstance().getUniverse() != CommandCenter.Universe.SMALL;
	}


	public void initGame(){
		clearAll();
		generateStarField();
		setLevel(0);
		setScore(0);
		setPaused(false);
		//set to one greater than number of falcons lives in your game as decrementFalconNumAndSpawn() also decrements
		setNumFalcons(4);
		falcon.decrementFalconNumAndSpawn();
		//add the falcon to the movFriends list
		opsQueue.enqueue(falcon, GameOp.Action.ADD);
		opsQueue.enqueue(miniMap, GameOp.Action.ADD);


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







}
