package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.controller.Game
import edu.uchicago.gerber._08final.mvc.controller.Sound.playSound
import lombok.Data
import java.awt.Point
import java.util.*


object CommandCenter {

     var numFalcons = 0
     var level = 0
     var score: Long = 0
     var paused = false
     var falcon = Falcon()

    //lists containing our movables
     val movDebris: MutableList<Movable> = LinkedList()
     val movFriends: MutableList<Movable> = LinkedList()
     val movFoes: MutableList<Movable> = LinkedList()
     val movFloaters: MutableList<Movable> = LinkedList()
     val opsQueue = GameOpsQueue()

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
        get() = numFalcons <= 0


}
