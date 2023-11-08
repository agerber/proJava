package edu.uchicago.gerber._08final.mvc.model;

import java.awt.*;
import java.util.LinkedList;

public interface Movable {

	enum Team {FRIEND, FOE, FLOATER, DEBRIS}

	//for the game to move and draw movable objects. See the GamePanel class.
	void move();
	void draw(Graphics g);

	//for collision detection
	Point getCenter();
	int getRadius();
	Team getTeam();
	boolean isProtected();


	//for adding or removing from game-space
	void add(LinkedList<Movable> list);

	void remove(LinkedList<Movable> list);


} //end Movable
