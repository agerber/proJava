package edu.uchicago.gerber._08final.mvc.model;



import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.controller.Sound;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

//the lombok @Data gives us automatic getters and setters on all members
@Data
public class CommandCenter {

	private  int numFalcons;
	private  int level;
	private  long score;
	//the falcon is located in the movFriends list, but since we use this reference a lot, we keep track of it in a
	//separate reference. Use final to ensure that the falcon ref always points to the falcon object.
	@Setter(value = AccessLevel.NONE)
	private final Falcon falcon  = new Falcon();
	private  boolean paused;

	private List<Movable> movDebris = new LinkedList<>();
	private List<Movable> movFriends = new LinkedList<>();
	private List<Movable> movFoes = new LinkedList<>();
	private List<Movable> movFloaters = new LinkedList<>();

	private GameOpsList opsList = new GameOpsList();


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


	public  void initGame(){
		setLevel(1);
		setScore(0);
		//set to one greater than number of falcons lives in your game as initFalconAndDecrementNum() also decrements
		setNumFalcons(4);
		initFalconAndDecrementFalconNum();
		opsList.enqueue(falcon, CollisionOp.Operation.ADD);

	}

	public  boolean isGameOver() {		//if the number of falcons is zero, then game over
		return getNumFalcons() == 0;
	}


	public void initFalconAndDecrementFalconNum(){
		setNumFalcons(getNumFalcons() - 1);
		if (isGameOver()) return;
		Sound.playSound("shipspawn.wav");
		falcon.setFade(Falcon.FADE_INITIAL_VALUE);
		//put falcon in the middle of the game-space
		falcon.setCenter(new Point(Game.DIM.width / 2, Game.DIM.height / 2));
		falcon.setOrientation(Game.R.nextInt(360));
		falcon.setDeltaX(0);
		falcon.setDeltaY(0);
	}

	public  void clearAll(){
		movDebris.clear();
		movFriends.clear();
		movFoes.clear();
		movFloaters.clear();
	}







}
