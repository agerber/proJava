package edu.uchicago.gerber._08final.mvc.controller

import edu.uchicago.gerber._08final.mvc.model.*
import edu.uchicago.gerber._08final.mvc.model.Movable.Team
import edu.uchicago.gerber._08final.mvc.view.GamePanel
import java.awt.Dimension
import java.awt.EventQueue
import java.awt.Point
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.*
import javax.sound.sampled.Clip

// ===============================================
// == This Game class is the CONTROLLER
// ===============================================
class Game : Runnable, KeyListener {
    private val gmpPanel: GamePanel
    private val animationThread: Thread

    //todo this is state: move to CommandCenter
    private var muted = true
    private val PAUSE = 80

    // p key
    private val QUIT = 81

    // q key
    private val LEFT = 37

    // rotate left; left arrow
    private val RIGHT = 39

    // rotate right; right arrow
    private val UP = 38

    // thrust; up arrow
    private val START = 83

    // s key
    private val FIRE = 32

    // space key
    private val MUTE = 77 // m-key mute

    // for possible future use
    // HYPER = 68, 					// D key
    // SHIELD = 65, 				// A key
    // SPECIAL = 70; 					// fire special weapon;  F key
    private val clpThrust: Clip
    private val clpMusicBackground: Clip

    // ===============================================
    // ==CONSTRUCTOR
    // ===============================================
    init {
        gmpPanel = GamePanel(DIM)
        gmpPanel.addKeyListener(this) //Game object implements KeyListener
        clpThrust = Sound.clipForLoopFactory("whitenoise.wav")
        clpMusicBackground = Sound.clipForLoopFactory("music-background.wav")

        //fire up the animation thread
        animationThread = Thread(this) // pass the animation thread a runnable object, the Game object
        animationThread.start()
    }

    // Game implements runnable, and must have run method
    override fun run() {

        // lower animation thread's priority, thereby yielding to the "main" aka 'Event Dispatch'
        // thread which listens to keystrokes
        animationThread.priority = Thread.MIN_PRIORITY

        // and get the current time
        var lStartTime = System.currentTimeMillis()

        // this thread animates the scene
        while (Thread.currentThread() === animationThread) {
            gmpPanel.update(gmpPanel.graphics) // see GamePanel class
            checkCollisions()
            checkNewLevel()
            spawnNewShipFloater()

            // surround the sleep() in a try/catch block
            // this simply controls delay time between
            // the frames of the animation
            try {
                // The total amount of time is guaranteed to be at least ANI_DELAY long.  If processing (update) 
                // between frames takes longer than ANI_DELAY, then the difference between lStartTime - 
                // System.currentTimeMillis() will be negative, then zero will be the sleep time
                lStartTime += ANI_DELAY.toLong()
                Thread.sleep(Math.max(0,
                        lStartTime - System.currentTimeMillis()))
            } catch (e: InterruptedException) {
                // do nothing (bury the exception), and just continue, e.g. skip this frame -- no big deal
            }
        } // end while
    } // end run

    private fun checkCollisions() {
        var pntFriendCenter: Point
        var pntFoeCenter: Point
        var radFriend: Int
        var radFoe: Int

        //This has order-of-growth of O(n^2), there is no way around this.
        for (movFriend in CommandCenter.instance.movFriends) {
            for (movFoe in CommandCenter.instance.movFoes) {
                pntFriendCenter = movFriend.center
                pntFoeCenter = movFoe.center
                radFriend = movFriend.radius
                radFoe = movFoe.radius

                //detect collision
                if (pntFriendCenter.distance(pntFoeCenter) < radFriend + radFoe) {
                    //remove the friend (so long as he is not protected)
                    if (!movFriend.isProtected) {
                        CommandCenter.instance.opsQueue.enqueue(movFriend, GameOp.Action.REMOVE)
                    }
                    //remove the foe
                    CommandCenter.instance.opsQueue.enqueue(movFoe, GameOp.Action.REMOVE)
                    Sound.playSound("kapow.wav")
                }
            } //end inner for
        } //end outer for

        //check for collisions between falcon and floaters. Order of growth of O(n) where n is number of floaters
        val pntFalCenter = CommandCenter.instance.falcon.center
        val radFalcon = CommandCenter.instance.falcon.radius
        var pntFloaterCenter: Point
        var radFloater: Int
        for (movFloater in CommandCenter.instance.movFloaters) {
            pntFloaterCenter = movFloater.center
            radFloater = movFloater.radius

            //detect collision
            if (pntFalCenter.distance(pntFloaterCenter) < radFalcon + radFloater) {
                CommandCenter.instance.opsQueue.enqueue(movFloater, GameOp.Action.REMOVE)
                Sound.playSound("pacman_eatghost.wav")
            } //end if
        } //end for
        processGameOpsQueue()
    } //end meth

    private fun processGameOpsQueue() {

        //deferred mutation: these operations are done AFTER we have completed our collision detection to avoid
        // mutating the movable linkedlists while iterating them above
        while (!CommandCenter.instance.opsQueue.isEmpty()) {
            val gameOp = CommandCenter.instance.opsQueue.dequeue()
            val mov = gameOp.movable
            val action = gameOp.action
            when (mov.team) {
                Team.FOE -> if (action == GameOp.Action.ADD) {
                    CommandCenter.instance.movFoes.add(mov)
                } else { //GameOp.Operation.REMOVE
                    CommandCenter.instance.movFoes.remove(mov)
                    if (mov is Asteroid) spawnSmallerAsteroids(mov)
                }

                Team.FRIEND -> if (action == GameOp.Action.ADD) {
                    CommandCenter.instance.movFriends.add(mov)
                } else { //GameOp.Operation.REMOVE
                    if (mov is Falcon) {
                        CommandCenter.instance.initFalconAndDecrementFalconNum()
                    } else {
                        CommandCenter.instance.movFriends.remove(mov)
                    }
                }

                Team.FLOATER -> if (action == GameOp.Action.ADD) {
                    CommandCenter.instance.movFloaters.add(mov)
                } else { //GameOp.Operation.REMOVE
                    CommandCenter.instance.movFloaters.remove(mov)
                }

                Team.DEBRIS -> if (action == GameOp.Action.ADD) {
                    CommandCenter.instance.movDebris.add(mov)
                } else { //GameOp.Operation.REMOVE
                    CommandCenter.instance.movDebris.remove(mov)
                }
            }
        }
    }

    private fun spawnNewShipFloater() {

        //appears more often as your level increases.
        if (System.currentTimeMillis() / ANI_DELAY % (SPAWN_NEW_SHIP_FLOATER - CommandCenter.instance.level * 7L) == 0L) {
            CommandCenter.instance.opsQueue.enqueue(NewShipFloater(), GameOp.Action.ADD)
        }
    }

    //this method spawns new Large (0) Asteroids
    private fun spawnBigAsteroids(nNum: Int) {
        var nNum = nNum
        while (nNum-- > 0) {
            //Asteroids with size of zero are big
            CommandCenter.instance.opsQueue.enqueue(Asteroid(0), GameOp.Action.ADD)
        }
    }

    private fun spawnSmallerAsteroids(originalAsteroid: Asteroid) {
        var nSize = originalAsteroid.size
        if (nSize > 1) return  //return if Small (2) Asteroid

        //for large (0) and medium (1) sized Asteroids only, spawn 2 or 3 smaller asteroids respectively
        nSize += 2
        while (nSize-- > 0) {
            CommandCenter.instance.opsQueue.enqueue(Asteroid(originalAsteroid), GameOp.Action.ADD)
        }
    }

    //if there are no more Asteroids on the screen
    private val isLevelClear: Boolean
        private get() {
            //if there are no more Asteroids on the screen
            var asteroidFree = true
            for (movFoe in CommandCenter.instance.movFoes) {
                if (movFoe is Asteroid) {
                    asteroidFree = false
                    break
                }
            }
            return asteroidFree
        }

    private fun checkNewLevel() {
        if (isLevelClear) {
            //more asteroids at each level to increase difficulty
            CommandCenter.instance.level = CommandCenter.instance.level + 1
            spawnBigAsteroids(CommandCenter.instance.level)
            //setFade e.g. protect the falcon so that player has time to avoid newly spawned asteroids.
            CommandCenter.instance.falcon.fade = Falcon.FADE_INITIAL_VALUE
        }
    }

    // ===============================================
    // KEYLISTENER METHODS
    // ===============================================
    override fun keyPressed(e: KeyEvent) {
        val fal = CommandCenter.instance.falcon
        val nKey = e.keyCode
        if (nKey == START && CommandCenter.instance.isGameOver) CommandCenter.instance.initGame()
        if (fal != null) {
            when (nKey) {
                PAUSE -> {
                    CommandCenter.instance.isPaused = !CommandCenter.instance.isPaused
                    if (CommandCenter.instance.isPaused) stopLoopingSounds(clpMusicBackground, clpThrust)
                }

                QUIT -> System.exit(0)
                UP -> {
                    fal.thrustOn()
                    if (!CommandCenter.instance.isPaused && !CommandCenter.instance.isGameOver) clpThrust.loop(Clip.LOOP_CONTINUOUSLY)
                }

                LEFT -> fal.rotateLeft()
                RIGHT -> fal.rotateRight()
                else -> {}
            }
        }
    }

    override fun keyReleased(e: KeyEvent) {
        val fal = CommandCenter.instance.falcon
        val nKey = e.keyCode
        //show the key-code in the console
        println(nKey)
        if (fal != null) {
            when (nKey) {
                FIRE -> {
                    CommandCenter.instance.opsQueue.enqueue(Bullet(fal), GameOp.Action.ADD)
                    Sound.playSound("laser.wav")
                }

                LEFT -> fal.stopRotating()
                RIGHT -> fal.stopRotating()
                UP -> {
                    fal.thrustOff()
                    clpThrust.stop()
                }

                MUTE -> {
                    if (!muted) {
                        stopLoopingSounds(clpMusicBackground)
                    } else {
                        clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY)
                    }
                    muted = !muted
                }

                else -> {}
            }
        }
    }

    // does nothing, but we need it b/c of KeyListener contract
    override fun keyTyped(e: KeyEvent) {}

    companion object {
        // ===============================================
        // FIELDS
        // ===============================================
		@JvmField
		val DIM = Dimension(1100, 900) //the dimension of the game.

        //this is used throughout many classes.
		@JvmField
		var R = Random()
        const val ANI_DELAY = 40 // milliseconds between screen

        // updates (animation)
        const val FRAMES_PER_SECOND = 1000 / ANI_DELAY

        //spawn every 30 seconds
        private const val SPAWN_NEW_SHIP_FLOATER = FRAMES_PER_SECOND * 30

        // ===============================================
        // ==METHODS
        // ===============================================
        @JvmStatic
        fun main(args: Array<String>) {
            //typical Swing application start; we pass EventQueue a Runnable object.
            EventQueue.invokeLater { Game() }
        }

        // Varargs for stopping looping-music-clips
        private fun stopLoopingSounds(vararg clpClips: Clip) {
            for (clp in clpClips) {
                clp.stop()
            }
        }
    }
}