package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.controller.Game
import edu.uchicago.gerber._08final.mvc.controller.Sound.playSound
import lombok.Data
import java.awt.Point
import java.util.*

//the lombok @Data gives us automatic getters and setters on all members
@Data
class CommandCenter  // Constructor made private
private constructor() {
    private var numFalcons = 0
    private var level = 0
    private var score: Long = 0
    private var paused = false

    //the falcon is located in the movFriends list, but since we use this reference a lot, we keep track of it in a
    //separate reference. Use final to ensure that the falcon ref always points to the single falcon object on heap
    //Lombok will not provide setter methods on final members
    private val falcon = Falcon()

    //lists containing our movables
    private val movDebris: MutableList<Movable> = LinkedList()
    private val movFriends: MutableList<Movable> = LinkedList()
    private val movFoes: MutableList<Movable> = LinkedList()
    private val movFloaters: MutableList<Movable> = LinkedList()
    private val opsQueue = GameOpsQueue()
    fun initGame() {
        clearAll()
        level = 1
        score = 0
        paused = false
        //set to one greater than number of falcons lives in your game as initFalconAndDecrementNum() also decrements
        numFalcons = 4
        initFalconAndDecrementFalconNum()
        opsQueue.enqueue(falcon, GameOp.Action.ADD)
    }

    fun initFalconAndDecrementFalconNum() {
        numFalcons -= 1
        if (isGameOver) return
        playSound("shipspawn.wav")
        falcon.fade = Falcon.FADE_INITIAL_VALUE
        //put falcon in the middle of the game-space
        falcon.setCenter(Point(Game.DIM.width / 2, Game.DIM.height / 2))
        falcon.orientation = Game.R.nextInt(360)
        falcon.deltaX = 0.0
        falcon.deltaY = 0.0
    }

    private fun clearAll() {
        movDebris.clear()
        movFriends.clear()
        movFoes.clear()
        movFloaters.clear()
    }

    //if the number of falcons is zero, then game over
    val isGameOver: Boolean
        get() =//if the number of falcons is zero, then game over
            numFalcons <= 0

    companion object {
        //this class maintains game state - make this a singleton.
        //singleton
		@JvmStatic
		var instance: CommandCenter? = null
            get() {
                if (field == null) {
                    field = CommandCenter()
                }
                return field
            }
            private set
    }
}