package edu.uchicago.gerber._08final.mvc.controller

import edu.uchicago.gerber._08final.mvc.model.*
import java.awt.Point
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import java.util.function.BiFunction
import java.util.stream.Collectors

object CommandCenter {

     var numFalcons = 0
     var level = 0
     var score: Long = 0L
     var paused = false
     var muted = true
     var frame: Long = 0L

    //the falcon should always point to this object on the heap
     val falcon = Falcon()

    //lists containing our movables
     val movDebris: MutableList<Movable> = LinkedList()
     val movFriends: MutableList<Movable> = LinkedList()
     val movFoes: MutableList<Movable> = LinkedList()
     val movFloaters: MutableList<Movable> = LinkedList()

     val opsQueue = GameOpsQueue()

    //for sound playing. Limit the number of threads to 5 at a time.
     val soundExecutor = Executors.newFixedThreadPool(5) as ThreadPoolExecutor


    fun initGame() {
        clearAll()
        level = 0
        score = 0
        paused = false
        //set to one greater than number of falcons lives in your game as initFalconAndDecrementNum() also decrements
        numFalcons = 4
        initFalconAndDecrementFalconNum()
        opsQueue.enqueue(falcon, GameOp.Action.ADD)
    }

     fun initFalconAndDecrementFalconNum() {
        numFalcons -= 1
        if (isGameOver()) return
        //playSound("shipspawn.wav")
        falcon.shield = Falcon.SPAWN_INIT_VALUE
        //put falcon in the middle of the game-space
        falcon.center = Point(Game.DIM.width / 2, Game.DIM.height / 2)
        falcon.orientation = Game.R.nextInt(360)
        falcon.deltaX = 0.0
        falcon.deltaY = 0.0
    }

     fun clearAll() {
        movDebris.clear()
        movFriends.clear()
        movFoes.clear()
        movFloaters.clear()
    }

    fun incrementFrame() {
        frame = if (frame < Long.MAX_VALUE) frame + 1 else 0
    }


    //if the number of falcons is zero, then game over
    fun isGameOver(): Boolean {
        return numFalcons <= 0
    }


    //Utility method for transforming cartesian points to polar points
     fun cartesianToPolar(pntCartesians: Array<Point>): Array<PolarPoint> {

        val cartToPolarTransform = BiFunction { pnt: Point, hyp: Double ->
            PolarPoint( //this is r from PolarPoint(r,theta).
                hypotFunction(pnt.x.toDouble(), pnt.y.toDouble()) / hyp,  //r is relative to the largestHypotenuse
                //this is theta from PolarPoint(r,theta)
                Math.toDegrees(Math.atan2(pnt.y.toDouble(), pnt.x.toDouble())) * Math.PI / 180
            )
        }


        //determine the largest hypotenuse
        var largestHypotenuse = 0.0
        for (pnt in pntCartesians){
            if (hypotFunction(pnt.x.toDouble(), pnt.y.toDouble()) > largestHypotenuse)
                largestHypotenuse = hypotFunction(pnt.x.toDouble(), pnt.y.toDouble())
        }


        //we must make hypotenuse final to pass into a stream.
        val hyp = largestHypotenuse
        return Arrays.stream(pntCartesians)
            .map { pnt: Point -> cartToPolarTransform.apply(pnt, hyp) }
            .collect(Collectors.toList())
            .toTypedArray()
    }

    //private helper method
    private fun hypotFunction(dX: Double, dY: Double): Double {
        return Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dY, 2.0))
    }


    fun pointsListToArray(pntPs: List<Point?>): Array<out Any> {
        return pntPs.stream()
            .toArray();
    }


}

