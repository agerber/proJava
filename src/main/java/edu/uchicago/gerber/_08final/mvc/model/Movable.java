package edu.uchicago.gerber._08final.mvc.model;

import java.awt.*;
import java.util.LinkedList;


/* TODO This interface is an example of the Facade design pattern which provides a simplified
interface to a complex subsystem or set of classes. It hides the complexity by offering a more straightforward and unified API.
The goal is to make subsystems easier to use by providing a higher-level interface that clients can interact with.
 */
public interface Movable {

	enum Team {FRIEND, FOE, FLOATER, DEBRIS}

	//for the game to move and draw movable objects. See the GamePanel class.
	void move();
	void draw(Graphics g);

	//for collision detection
	Point getCenter();
	int getRadius();
	Team getTeam();



	/* TODO The following methods are examples of Observer design pattern (Lifecycle Callbacks). Lifecycle
	 Callbacks allow an object to perform specific actions at
	well-defined stages of its lifecycle. Lifecycle Callbacks encapsulate logic that would otherwise be scattered throughout
	other classes, thereby making the code more organized and easier to manage.

	Subject (Game): The Game class acts as the subject that triggers changes in the state of Movable objects in the
	processGameOpsQueue() method.

	Observer (Movable): Each Movable object implements the lifecycle methods (addToGame, removeFromGame). These methods are
	called by the Game's processGameOpsQueue() method to notify the Movable objects about their state changes (e.g.,
	being added to or removed from the game).

	  */

	//this is your opportunity to add sounds or perform other side effects.
	//The 'list' parameter will be one of the following: movFriends, movFoes, movDebris, movFloaters.
	//See processGameOpsQueue() of Game class
	void addToGame(LinkedList<Movable> list);

	void removeFromGame(LinkedList<Movable> list);


} //end Movable
