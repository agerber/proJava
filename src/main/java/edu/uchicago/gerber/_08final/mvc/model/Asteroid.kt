package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.controller.Game
import edu.uchicago.gerber._08final.mvc.model.Movable.Team
import java.awt.Point
import java.util.*
import java.util.function.Supplier
import java.util.stream.Collectors
import java.util.stream.Stream

class Asteroid(size: Int) : Sprite() {
    //radius of a large asteroid
    private val LARGE_RADIUS = 100

    //size determines if the Asteroid is Large (0), Medium (1), or Small (2)
    //when you explode a Large asteroid, you should spawn 2 or 3 medium asteroids
    //same for medium asteroid, you should spawn small asteroids
    // get blasted into debris, but do not spawn anything
    init {

        //Asteroid is FOE
        team = Team.FOE
        //the spin will be either plus or minus 0-9
        spin = somePosNegValue(10)
        //random delta-x
        deltaX = somePosNegValue(10).toDouble()
        //random delta-y
        deltaY = somePosNegValue(10).toDouble()

        //a size of zero is a big asteroid
        //a size of 1 or 2 is med or small asteroid respectively. See getSize() method.
        radius = if (size == 0) LARGE_RADIUS else LARGE_RADIUS / (size * 2)
        cartesians = Arrays.asList(genRandomPoints())
    }

    //overloaded so we can spawn smaller asteroids from an exploding one
    constructor(astExploded: Asteroid) : this(astExploded.size + 1) {
        //calls the other constructor: Asteroid(int size)
        center = astExploded.center
        val newSmallerSize = astExploded.size + 1
        //random delta-x : inertia + the smaller the asteroid, the faster its possible speed
        deltaX = astExploded.deltaX / 1.5 + somePosNegValue(5 + newSmallerSize * 2)
        //random delta-y : inertia + the smaller the asteroid, the faster its possible speed
        deltaY = astExploded.deltaY / 1.5 + somePosNegValue(5 + newSmallerSize * 2)
    }

    val size: Int
        get() = when (radius) {
            LARGE_RADIUS -> 0
            LARGE_RADIUS / 2 -> 1
            LARGE_RADIUS / 4 -> 2
            else -> 0
        }

    override fun move() {
        super.move()
        //an asteroid spins, so you need to adjust the orientation at each move()
        orientation += spin
    }



    private fun genRandomPoints(): Array<Point> {
        //6.283 is the max radians
        val MAX_RADIANS_X1000 = 6283
        val polarPointSupplier = Supplier {
            val r = (800 + Game.R.nextInt(200)) / 1000.0 //number between 0.8 and 0.999
            val theta = Game.R.nextInt(MAX_RADIANS_X1000) / 1000.0 // number between 0 and 6.282
            PolarPoint(r, theta)
        }

        //random number of vertices between 17 and 23
        val vertices = Game.R.nextInt(7) + 17
        return polarToCartesian(
                Stream.generate(polarPointSupplier)
                        .limit(vertices.toLong())
                        .sorted { pp1, pp2 -> pp1.theta.compareTo(pp2.theta) }
                        .collect(Collectors.toList())

        )
    }
}
