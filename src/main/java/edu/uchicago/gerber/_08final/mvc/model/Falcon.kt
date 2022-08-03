package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.model.Movable.Team
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.util.*

class Falcon : Sprite() {
    // ==============================================================
    // FIELDS 
    // ==============================================================
    companion object {
        const val THRUST = .65
        const val DEGREE_STEP = 9
        const val FADE_INITIAL_VALUE = 51
    }

    //private boolean shield = false;
     var thrusting = false
     var turningRight = false
     var turningLeft = false

    // ==============================================================
    // CONSTRUCTOR 
    // ==============================================================
    init {
        team = Team.FRIEND

        //this is the size (radius) of the falcon
        radius = 35
        val pntCs = ArrayList<Point>()
        // Robert Alef's awesome falcon design
        pntCs.add(Point(0, 9))
        pntCs.add(Point(-1, 6))
        pntCs.add(Point(-1, 3))
        pntCs.add(Point(-4, 1))
        pntCs.add(Point(4, 1))
        pntCs.add(Point(-4, 1))
        pntCs.add(Point(-4, -2))
        pntCs.add(Point(-1, -2))
        pntCs.add(Point(-1, -9))
        pntCs.add(Point(-1, -2))
        pntCs.add(Point(-4, -2))
        pntCs.add(Point(-10, -8))
        pntCs.add(Point(-5, -9))
        pntCs.add(Point(-7, -11))
        pntCs.add(Point(-4, -11))
        pntCs.add(Point(-2, -9))
        pntCs.add(Point(-2, -10))
        pntCs.add(Point(-1, -10))
        pntCs.add(Point(-1, -9))
        pntCs.add(Point(1, -9))
        pntCs.add(Point(1, -10))
        pntCs.add(Point(2, -10))
        pntCs.add(Point(2, -9))
        pntCs.add(Point(4, -11))
        pntCs.add(Point(7, -11))
        pntCs.add(Point(5, -9))
        pntCs.add(Point(10, -8))
        pntCs.add(Point(4, -2))
        pntCs.add(Point(1, -2))
        pntCs.add(Point(1, -9))
        pntCs.add(Point(1, -2))
        pntCs.add(Point(4, -2))
        pntCs.add(Point(4, 1))
        pntCs.add(Point(1, 3))
        pntCs.add(Point(1, 6))
        pntCs.add(Point(0, 9))
        cartesians = pntCs
    }

    override fun isProtected(): Boolean {
        return fade < 255
    }

    // ==============================================================
    // METHODS 
    // ==============================================================
    override fun move() {
        super.move()
        if (isProtected()) {
            fade = fade + 3
        }

        //apply some thrust vectors using trig.
        if (thrusting) {
            val adjustX = (Math.cos(Math.toRadians(orientation.toDouble()))
                    * THRUST)
            val adjustY = (Math.sin(Math.toRadians(orientation.toDouble()))
                    * THRUST)
            deltaX = deltaX + adjustX
            deltaY = deltaY + adjustY
        }
        //rotate left
        if (turningLeft) {
            if (orientation <= 0) {
                orientation = 360
            }
            orientation = orientation - DEGREE_STEP
        }
        //rotate right
        if (turningRight) {
            if (orientation >= 360) {
                orientation = 0
            }
            orientation = orientation + DEGREE_STEP
        }
    } //end move

    //methods for moving the falcon
    fun rotateLeft() {
        turningLeft = true
    }

    fun rotateRight() {
        turningRight = true
    }

    fun stopRotating() {
        turningRight = false
        turningLeft = false
    }

    fun thrustOn() {
        thrusting = true
    }

    fun thrustOff() {
        thrusting = false
    }

    private fun adjustColor(colorNum: Int, adjust: Int): Int {
        return Math.max(colorNum - adjust, 0)
    }

    override fun draw(g: Graphics?) {
        val colShip: Color
        colShip = if (fade == 255) {
            color //get native color of the sprite
        } else if (fade > 220 && fade % 9 == 0) {
            Color(0, 32, 128) //dark blue
        } else {
            Color(
                    adjustColor(fade, 200),  //red
                    adjustColor(fade, 175),  //green
                    fade //blue
            )
        }

        //most Sprites do not have flames, but Falcon does
        val flames = doubleArrayOf(23 * Math.PI / 24 + Math.PI / 2, Math.PI + Math.PI / 2, 25 * Math.PI / 24 + Math.PI / 2)
        val pntFlames = arrayOfNulls<Point>(flames.size)

        //thrusting
        if (thrusting) {
            //the flame
            for (nC in flames.indices) {
                if (nC % 2 != 0) //odd
                {
                    //adjust the position so that the flame is off-center
                    pntFlames[nC] = Point((center.x + (2
                            * radius
                            * Math.sin(Math.toRadians(orientation.toDouble())
                            + flames[nC]))).toInt(), (center.y - (2
                            * radius
                            * Math.cos(Math.toRadians(orientation.toDouble())
                            + flames[nC]))).toInt())
                } else  //even
                {
                    pntFlames[nC] = Point((center.x + (radius
                            * 1.1
                            * Math.sin(Math.toRadians(orientation.toDouble())
                            + flames[nC]))).toInt(), (center.y - (radius
                            * 1.1
                            * Math.cos(Math.toRadians(orientation.toDouble())
                            + flames[nC]))).toInt())
                } //end even/odd else
            } //end for loop
            g?.color = colShip //flames same color as ship
            g?.fillPolygon(
                    Arrays.stream(pntFlames)
                            .map { pnt: Point? -> pnt!!.x }
                            .mapToInt { obj: Int -> obj }
                            .toArray(),
                    Arrays.stream(pntFlames)
                            .map { pnt: Point? -> pnt!!.y }
                            .mapToInt { obj: Int -> obj }
                            .toArray(),
                    flames.size)
        } //end if flame
        if (g != null) {
            draw(g, colShip)
        }
    } //end draw()


} //end class
