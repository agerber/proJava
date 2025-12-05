package edu.uchicago.gerber._08final.mvc.controller;


import java.awt.*;
import edu.uchicago.gerber._08final.mvc.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

//The CommandCenter is a singleton that manages the state of the game.
//the lombok @Data gives us automatic getters and setters on all members
@Data
public class CommandCenter {


	private  int numFalcons;
	private  int level;
	private  long score;
	private  boolean paused;
	private  boolean themeMusic;
	private boolean radarToggle; //to toggle on/off the radar
	//this value is used to count the number of frames (full animation cycles) in the game
	private long frame;

	//the falcon is located in the movFriends list, but since we use this reference a lot, we keep track of it in a
	//separate reference. Use final to ensure that the falcon ref always points to the single falcon object on heap.
	//Lombok will not provide setter methods on final members
	private final Falcon falcon  = new Falcon();
	private final Radar radar = new Radar();

	private final Universe[] universes =  new Universe[] {
			new Universe("FREE FLY", new Dimension(1, 1)),
			new Universe("CENTER", new Dimension(1, 1)),
			new Universe("BIG", new Dimension(2, 2)),
			new Universe("HORIZONTAL", new Dimension(3, 1)),
			new Universe("VERTICAL", new Dimension(1, 3)),
			new Universe("DARK", new Dimension(4, 4))

	};
	/*
	 TODO The following LinkedList<Movable> are examples of the Composite design pattern which is used to allow
	 compositions of objects to be treated uniformly. Here are the elements of the Composite design pattern:

     Component: Movable serves as the component interface. It defines common methods (move(), draw(Graphics g), etc.)
     that all concrete implementing classes must provide.

     Leaf: Concrete classes that implement Movable (e.g., Bullet, Asteroid) are the leaf nodes. They implement the
     Movable interface and provide specific behavior.

     Composite: The LinkedLists below that aggregate Movable objects (e.g., movFriends, movFoes) act as
     composites. They manage collections of Movable objects and provide a way to iterate over and operate on them as a
     group.

	 */
	private final LinkedList<Movable> movDebris = new LinkedList<>();
	private final LinkedList<Movable> movFriends = new LinkedList<>();
	private final LinkedList<Movable> movFoes = new LinkedList<>();
	private final LinkedList<Movable> movFloaters = new LinkedList<>();

	private final GameOpsQueue opsQueue = new GameOpsQueue();



	/* TODO This is an example of the Singleton design pattern. The Singleton ensures that a class has one (and only
	one) instance on the heap and provides a global point of access at instance. This is useful when you need to
	coordinate actions among objects in your system or manage state. CommandCenter manages the state of the game.
	 */
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


	public void initGame(){

		clearAll();
		generateStarField();
		setLevel(0);
		setRadarToggle(true);
		setScore(0);
		setPaused(false);
		//set to one greater than number of falcons lives in your game as decrementFalconNumAndSpawn() also decrements
		setNumFalcons(4);
		falcon.decrementFalconNumAndSpawn();
		//The following two objects are initialized at start game. All other sprites should be enqueued during gameplay
		opsQueue.enqueue(falcon, GameOp.Action.ADD);
		opsQueue.enqueue(radar, GameOp.Action.ADD);
		//theme music
		SoundLoader.playSound("dr_loop.wav");
		setThemeMusic(true);


	}


	private void generateStarField(){

		int count = 100;
		while (count-- > 0){
			opsQueue.enqueue(new Star(), GameOp.Action.ADD);
		}
	}

	public void incrementFrame(){
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
		Optional<Universe> universe = optUni();
		return universe.isEmpty() ? new Dimension(1,1) : universe.get().getDimension();

	}

	public String getUniName(){
		Optional<Universe> universe = optUni();
		return universe.isEmpty() ? "" : universe.get().getName();
	}

	public boolean isFalconPositionFixed(){
		Optional<Universe> universe = optUni();
		return universe.isEmpty() ? true : (!universe.get().getName().equalsIgnoreCase("FREE FLY"));
	}
	private Optional<Universe> optUni() {
		if (getLevel() == 0) {
			return Optional.empty();
		}
		int index = (getLevel() - 1) % universes.length;
		return Optional.of(universes[index]);
	}

	//inner class
	@Data
	@AllArgsConstructor
	class Universe {
		private String name;
		private Dimension dimension;
	}

}
