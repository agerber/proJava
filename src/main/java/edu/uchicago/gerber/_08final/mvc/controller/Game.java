package edu.uchicago.gerber._08final.mvc.controller;

import edu.uchicago.gerber._08final.mvc.model.*;
import edu.uchicago.gerber._08final.mvc.view.GamePanel;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;


// ===============================================
// == This Game class is the CONTROLLER
// ===============================================

public class Game implements Runnable, KeyListener {

    // ===============================================
    // FIELDS
    // ===============================================

    public static final Dimension DIM = new Dimension(1400,900); //the dimension of the game-screen.



    private final GamePanel gamePanel;
    //this is used throughout many classes.
    public static final Random R = new Random();

    public final static int ANIMATION_DELAY = 40; // milliseconds between frames

    public final static int FRAMES_PER_SECOND = 1000 / ANIMATION_DELAY;

    private final Thread animationThread;


    //key-codes
    private static final int
            PAUSE = 80, // p key
            QUIT = 81, // q key
            LEFT = 37, // rotate left; left arrow
            RIGHT = 39, // rotate right; right arrow
            UP = 38, // thrust; up arrow
            START = 83, // s key
            FIRE = 32, // space key
            MUTE = 77, // m-key mute

            UNIVERSE = 65, //a-key

            NUKE = 70; // f-key mute

    // for possible future use
    // HYPER = 68, 					// D key
    //ALIEN = 65;                // A key



    // ===============================================
    // ==CONSTRUCTOR
    // ===============================================

    public Game() {

        gamePanel = new GamePanel(DIM);
        gamePanel.addKeyListener(this); //Game object implements KeyListener
        //fire up the animation thread
        animationThread = new Thread(this); // pass the animation thread a runnable object, the Game object
        animationThread.setDaemon(true);
        animationThread.start();


    }

    // ===============================================
    // ==METHODS
    // ===============================================

    public static void main(String[] args) {


        //typical Swing application start; we pass EventQueue a Runnable object.
        EventQueue.invokeLater(Game::new);
    }

    // Game implements runnable, and must have run method
    @Override
    public void run() {

        // lower animation thread's priority, thereby yielding to the 'Event Dispatch Thread' or EDT
        // thread which listens to keystrokes
        animationThread.setPriority(Thread.MIN_PRIORITY);

        // and get the current time
        long startTime = System.currentTimeMillis();

        // this thread animates the scene
        while (Thread.currentThread() == animationThread) {


            //this call will cause all movables to move() and draw() themselves every ~40ms
            // see GamePanel class for details
            gamePanel.update(gamePanel.getGraphics());

            checkCollisions();
            checkNewLevel();
            checkFloaters();
            //this method will execute add() and remove() callbacks on Movable objects
            processGameOpsQueue();
            //keep track of the frame for development purposes
            CommandCenter.getInstance().incrementFrame();

            // surround the sleep() in a try/catch block
            // this simply controls delay time between
            // the frames of the animation
            try {
                // The total amount of time is guaranteed to be at least ANIMATION_DELAY long.  If processing (update)
                // between frames takes longer than ANIMATION_DELAY, then the difference between startTime -
                // System.currentTimeMillis() will be negative, then zero will be the sleep time
                startTime += ANIMATION_DELAY;

                Thread.sleep(Math.max(0,
                        startTime - System.currentTimeMillis()));
            } catch (InterruptedException e) {
                // do nothing (bury the exception), and just continue, e.g. skip this frame -- no big deal
            }
        } // end while
    } // end run

    private void checkFloaters() {

        spawnShieldFloater();
        spawnNukeFloater();
    }


    private void checkCollisions() {

        //This has order-of-growth of O(FOES * FRIENDS)
        Point pntFriendCenter, pntFoeCenter;
        int radFriend, radFoe;
        for (Movable movFriend : CommandCenter.getInstance().getMovFriends()) {
            for (Movable movFoe : CommandCenter.getInstance().getMovFoes()) {

                pntFriendCenter = movFriend.getCenter();
                pntFoeCenter = movFoe.getCenter();
                radFriend = movFriend.getRadius();
                radFoe = movFoe.getRadius();

                //detect collision
                if (pntFriendCenter.distance(pntFoeCenter) < (radFriend + radFoe)) {
                    //enqueue the friend
                    CommandCenter.getInstance().getOpsQueue().enqueue(movFriend, GameOp.Action.REMOVE);
                    //enqueue the foe
                    CommandCenter.getInstance().getOpsQueue().enqueue(movFoe, GameOp.Action.REMOVE);
                }
            }//end inner for
        }//end outer for

        //check for collisions between falcon and floaters. Order of growth of O(FLOATERS)
        Point pntFalCenter = CommandCenter.getInstance().getFalcon().getCenter();
        int radFalcon = CommandCenter.getInstance().getFalcon().getRadius();

        Point pntFloaterCenter;
        int radFloater;
        for (Movable movFloater : CommandCenter.getInstance().getMovFloaters()) {
            pntFloaterCenter = movFloater.getCenter();
            radFloater = movFloater.getRadius();
            //detect collision
            if (pntFalCenter.distance(pntFloaterCenter) < (radFalcon + radFloater)) {
                //enqueue the floater
                CommandCenter.getInstance().getOpsQueue().enqueue(movFloater, GameOp.Action.REMOVE);
            }//end if
        }//end for

    }//end meth


    //This method adds and removes movables to/from their respective linked-lists.
    private void processGameOpsQueue() {

        //deferred mutation: these operations are done AFTER we have completed our collision detection to avoid
        // mutating the movable linkedlists while iterating them above.
        while (!CommandCenter.getInstance().getOpsQueue().isEmpty()) {

            GameOp gameOp = CommandCenter.getInstance().getOpsQueue().dequeue();

            //given team, determine which linked-list this object will be added-to or removed-from
            LinkedList<Movable> list;
            Movable mov = gameOp.getMovable();
            switch (mov.getTeam()) {
                case FOE:
                    list = CommandCenter.getInstance().getMovFoes();
                    break;
                case FRIEND:
                    list = CommandCenter.getInstance().getMovFriends();
                    break;
                case FLOATER:
                    list = CommandCenter.getInstance().getMovFloaters();
                    break;
                case DEBRIS:
                default:
                    list = CommandCenter.getInstance().getMovDebris();
            }

            //pass the appropriate linked-list from above
            //this block will execute the addToGame() or removeFromGame() callbacks in the Movable models.
            GameOp.Action action = gameOp.getAction();
            if (action == GameOp.Action.ADD)
                mov.addToGame(list);
            else //REMOVE
                mov.removeFromGame(list);

        }//end while
    }





    private void spawnShieldFloater() {

        if (CommandCenter.getInstance().getFrame() % ShieldFloater.SPAWN_SHIELD_FLOATER == 0) {
            CommandCenter.getInstance().getOpsQueue().enqueue(new ShieldFloater(), GameOp.Action.ADD);
        }
    }

    private void spawnNukeFloater() {

        if (CommandCenter.getInstance().getFrame() % NukeFloater.SPAWN_NUKE_FLOATER == 0) {
            CommandCenter.getInstance().getOpsQueue().enqueue(new NukeFloater(), GameOp.Action.ADD);
        }
    }


    //this method spawns new Large (0) Asteroids
    private void spawnBigAsteroids(int num) {

        while (num-- > 0) {
            //Asteroids with size of zero are big
            CommandCenter.getInstance().getOpsQueue().enqueue(new Asteroid(0), GameOp.Action.ADD);

        }
    }





    private boolean isLevelClear() {
        //if there are no more Asteroids on the screen
        boolean asteroidFree = true;
        for (Movable movFoe : CommandCenter.getInstance().getMovFoes()) {
            if (movFoe instanceof Asteroid) {
                asteroidFree = false;
                break;
            }
        }
        return asteroidFree;
    }

    private void checkNewLevel() {

        if (isLevelClear()) {
            //currentLevel will be zero at beginning of game
            int level = CommandCenter.getInstance().getLevel();
            //award some points for having cleared the previous level
            CommandCenter.getInstance().setScore(CommandCenter.getInstance().getScore() + (10_000L * level));

            //bump the level up
            level = level + 1;
            CommandCenter.getInstance().setLevel(level);
            //spawn some big new asteroids
            spawnBigAsteroids(level);
            //make falcon invincible momentarily in case new asteroids spawn on top of him, and give player
            //time to adjust to new asteroids in game space.
            if (CommandCenter.getInstance().getFalcon().getShield() < Falcon.INITIAL_SPAWN_TIME)
                CommandCenter.getInstance().getFalcon().setShield(Falcon.INITIAL_SPAWN_TIME);
            //show "Level X" in middle of screen
            CommandCenter.getInstance().getFalcon().setShowLevel(Falcon.INITIAL_SPAWN_TIME);

        }
    }




    // ===============================================
    // KEYLISTENER METHODS
    // ===============================================

    @Override
    public void keyPressed(KeyEvent e) {
        Falcon falcon = CommandCenter.getInstance().getFalcon();
        int keyCode = e.getKeyCode();

        if (keyCode == START && CommandCenter.getInstance().isGameOver()) {
            CommandCenter.getInstance().initGame();
            return;
        }


        switch (keyCode) {
            case PAUSE:
                CommandCenter.getInstance().setPaused(!CommandCenter.getInstance().isPaused());
                break;
            case QUIT:
                System.exit(0);
                break;
            case UP:
                falcon.setThrusting(true);
                SoundLoader.playSound("whitenoise_loop.wav");
                break;
            case LEFT:
                falcon.setTurnState(Falcon.TurnState.LEFT);
                break;
            case RIGHT:
                falcon.setTurnState(Falcon.TurnState.RIGHT);
                break;


            // possible future use
            // case KILL:
            // case SHIELD:
            // case NUM_ENTER:

            default:
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        Falcon falcon = CommandCenter.getInstance().getFalcon();
        int keyCode = e.getKeyCode();
        //show the key-code in the console
        System.out.println(keyCode);

        switch (keyCode) {
            case FIRE:
                CommandCenter.getInstance().getOpsQueue().enqueue(new Bullet(falcon), GameOp.Action.ADD);
                break;
            case NUKE:
                CommandCenter.getInstance().getOpsQueue().enqueue(new Nuke(falcon), GameOp.Action.ADD);
                break;
            //releasing either the LEFT or RIGHT arrow key will set the TurnState to IDLE
            case LEFT:
            case RIGHT:
                falcon.setTurnState(Falcon.TurnState.IDLE);
                break;
            case UP:
                falcon.setThrusting(false);
                SoundLoader.stopSound("whitenoise_loop.wav");
                break;

            case MUTE:
                CommandCenter.getInstance().setThemeMusic(!CommandCenter.getInstance().isThemeMusic());
                if (!CommandCenter.getInstance().isThemeMusic()) {
                    SoundLoader.stopSound("theme_loop.wav");
                } else {
                    SoundLoader.playSound("theme_loop.wav");
                }
                break;
            case UNIVERSE:
                CommandCenter.getInstance().cycleUniverse();
                break;
        }

    }

    @Override
    // does nothing, but we need it b/c of KeyListener contract
    public void keyTyped(KeyEvent e) {
    }

}


