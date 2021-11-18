package edu.uchicago.gerber._08final.mvc.model;



import edu.uchicago.gerber._08final.mvc.controller.Sound;

import java.util.ArrayList;
import java.util.List;


public class CommandCenter {

	private  int numFalcons;
	private  int level;
	private  long score;
	private  Falcon falShip;
	private  boolean playing;
	private  boolean paused;
	
	// These ArrayLists with capacities set
	private List<Movable> movDebris = new ArrayList<Movable>(50);
	private List<Movable> movFriends = new ArrayList<Movable>(20);
	private List<Movable> movFoes = new ArrayList<Movable>(20);
	private List<Movable> movFloaters = new ArrayList<Movable>(5);

	private GameOpsList opsList = new GameOpsList();


	private static CommandCenter instance = null;

	// Constructor made private - static Utility class only
	private CommandCenter() {}


	public static CommandCenter getInstance(){
		if (instance == null){
			instance = new CommandCenter();
		}
		return instance;
	}


	public  void initGame(){
		setLevel(1);
		setScore(0);
		setNumFalcons(4);
		spawnFalcon();
	}

	public  void spawnFalcon() {
		if (getNumFalcons() != 0) {
			falShip = new Falcon();
			opsList.enqueue(falShip, CollisionOp.Operation.ADD);
			setNumFalcons(getNumFalcons() - 1);
		}
		Sound.playSound("shipspawn.wav");

	}

	public GameOpsList getOpsList() {
		return opsList;
	}

	public void setOpsList(GameOpsList opsList) {
		this.opsList = opsList;
	}

	public  void clearAll(){
		movDebris.clear();
		movFriends.clear();
		movFoes.clear();
		movFloaters.clear();
	}

	public  boolean isPlaying() {
		return playing;
	}

	public  void setPlaying(boolean bPlaying) {
		this.playing = bPlaying;
	}

	public  boolean isPaused() {
		return paused;
	}

	public  void setPaused(boolean bPaused) {
		this.paused = bPaused;
	}
	
	public  boolean isGameOver() {		//if the number of falcons is zero, then game over
		return getNumFalcons() == 0;
	}

	public  int getLevel() {
		return level;
	}

	public   long getScore() {
		return score;
	}

	public  void setScore(long score) {
		this.score = score;
	}

	public  void setLevel(int level) {
		this.level = level;
	}

	public  int getNumFalcons() {
		return numFalcons;
	}

	public  void setNumFalcons(int numFalcons) {
		this.numFalcons = numFalcons;
	}
	
	public  Falcon getFalcon(){
		return falShip;
	}
	
	public  void setFalcon(Falcon falParam){
		falShip = falParam;
	}

	public  List<Movable> getMovDebris() {
		return movDebris;
	}



	public  List<Movable> getMovFriends() {
		return movFriends;
	}



	public  List<Movable> getMovFoes() {
		return movFoes;
	}


	public  List<Movable> getMovFloaters() {
		return movFloaters;
	}




}
