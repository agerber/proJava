package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.model.Movable.Team
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.util.*


class Falcon : Sprite() { //end class
    // ==============================================================
    // FIELDS 
    // ==============================================================
    companion object {
        private const val THRUST = .65
        private const val DEGREE_STEP = 9
        const val FADE_INITIAL_VALUE = 51
    }

    var thrusting = false

    enum class TurnState {
        IDLE, LEFT, RIGHT
    }

    var turnState = TurnState.IDLE

    //used to toggle between the two shapes of this sprite
    var pntAlien: List<Point>
    var pntShip: List<Point>


    // ==============================================================
    // CONSTRUCTOR 
    // ==============================================================
    init {
        team = Team.FRIEND
        //this is the size (radius) of the falcon
        radius = 35
        val listShip = ArrayList<Point>()
        // Robert Alef's awesome falcon design
        listShip.add(Point(0, 9))
        listShip.add(Point(-1, 6))
        listShip.add(Point(-1, 3))
        listShip.add(Point(-4, 1))
        listShip.add(Point(4, 1))
        listShip.add(Point(-4, 1))
        listShip.add(Point(-4, -2))
        listShip.add(Point(-1, -2))
        listShip.add(Point(-1, -9))
        listShip.add(Point(-1, -2))
        listShip.add(Point(-4, -2))
        listShip.add(Point(-10, -8))
        listShip.add(Point(-5, -9))
        listShip.add(Point(-7, -11))
        listShip.add(Point(-4, -11))
        listShip.add(Point(-2, -9))
        listShip.add(Point(-2, -10))
        listShip.add(Point(-1, -10))
        listShip.add(Point(-1, -9))
        listShip.add(Point(1, -9))
        listShip.add(Point(1, -10))
        listShip.add(Point(2, -10))
        listShip.add(Point(2, -9))
        listShip.add(Point(4, -11))
        listShip.add(Point(7, -11))
        listShip.add(Point(5, -9))
        listShip.add(Point(10, -8))
        listShip.add(Point(4, -2))
        listShip.add(Point(1, -2))
        listShip.add(Point(1, -9))
        listShip.add(Point(1, -2))
        listShip.add(Point(4, -2))
        listShip.add(Point(4, 1))
        listShip.add(Point(1, 3))
        listShip.add(Point(1, 6))
        listShip.add(Point(0, 9))

        //Danica Gutierrez' Alien
        val listAlien: MutableList<Point> = ArrayList()
        listAlien.add(Point(0, 2))
        listAlien.add(Point(1, 2))
        listAlien.add(Point(1, 3))
        listAlien.add(Point(2, 3))
        listAlien.add(Point(2, 4))
        listAlien.add(Point(3, 4))
        listAlien.add(Point(3, 3))
        listAlien.add(Point(2, 3))
        listAlien.add(Point(2, 2))
        listAlien.add(Point(3, 2))
        listAlien.add(Point(3, 1))
        listAlien.add(Point(4, 1))
        listAlien.add(Point(4, 0))
        listAlien.add(Point(5, 0))
        //bottom right
        listAlien.add(Point(5, 0))
        listAlien.add(Point(5, -3))
        listAlien.add(Point(4, -3))
        listAlien.add(Point(4, -1))
        listAlien.add(Point(3, -1))
        listAlien.add(Point(3, -3))
        listAlien.add(Point(2, -3))
        listAlien.add(Point(2, -4))
        listAlien.add(Point(1, -4))
        listAlien.add(Point(1, -3))
        listAlien.add(Point(2, -3))
        listAlien.add(Point(2, -2))
        listAlien.add(Point(1, -2))
        listAlien.add(Point(0, -2))
        //bottom left quadrant
        listAlien.add(Point(-2, -2))
        listAlien.add(Point(-2, -3))
        listAlien.add(Point(-1, -3))
        listAlien.add(Point(-1, -4))
        listAlien.add(Point(-2, -4))
        listAlien.add(Point(-2, -3))
        listAlien.add(Point(-3, -3))
        listAlien.add(Point(-3, -1))
        listAlien.add(Point(-4, -1))
        listAlien.add(Point(-4, -3))
        listAlien.add(Point(-5, -3))
        listAlien.add(Point(-5, 0))
        //top left quadrant
        listAlien.add(Point(-5, 0))
        listAlien.add(Point(-4, 0))
        listAlien.add(Point(-4, 1))
        listAlien.add(Point(-3, 1))
        listAlien.add(Point(-3, 2))
        listAlien.add(Point(-2, 2))
        listAlien.add(Point(-2, 3))
        listAlien.add(Point(-3, 3))
        listAlien.add(Point(-3, 4))
        listAlien.add(Point(-2, 4))
        listAlien.add(Point(-2, 3))
        listAlien.add(Point(-1, 3))
        listAlien.add(Point(-1, 2))
        listAlien.add(Point(0, 2))

        //we need to create members for these points (unlike other Sprites) because we are morphing between ship/alien
        pntAlien = listAlien
        pntShip = listShip

        //default value of cartesians is ship
        cartesians = listShip
    }

    //has no functional value, but demonstrates how to morph a sprite
    fun toggleAlien(alien: Boolean) {
        if (alien) {
            cartesians = pntAlien
        } else {
            cartesians = pntShip
        }
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

        when (turnState) {
            TurnState.LEFT -> {
                if (orientation <= 0) {
                    orientation = 360
                }
                orientation = orientation - DEGREE_STEP
            }

            TurnState.RIGHT -> {
                if (orientation >= 360) {
                    orientation = 0
                }
                orientation = orientation + DEGREE_STEP
            }

            else -> {
                //do nothing
            }

        }

    } //end move

    //methods for moving the falcon
    fun rotateLeft() {
        turnState = TurnState.LEFT
    }

    fun rotateRight() {
        turnState = TurnState.RIGHT
    }

    fun stopRotating() {
        turnState = TurnState.IDLE
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

    override fun draw(g: Graphics) {
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
        val flames =
            doubleArrayOf(23 * Math.PI / 24 + Math.PI / 2, Math.PI + Math.PI / 2, 25 * Math.PI / 24 + Math.PI / 2)
        val pntFlames = arrayOfNulls<Point>(flames.size)

        //thrusting
        if (thrusting) {
            //the flame
            for (nC in flames.indices) {
                if (nC % 2 != 0) //odd
                {
                    //adjust the position so that the flame is off-center
                    pntFlames[nC] = Point(
                        (center.x + (2
                                * radius
                                * Math.sin(
                            Math.toRadians(orientation.toDouble())
                                    + flames[nC]
                        ))).toInt(), (center.y - (2
                                * radius
                                * Math.cos(
                            Math.toRadians(orientation.toDouble())
                                    + flames[nC]
                        ))).toInt()
                    )
                } else  //even
                {
                    pntFlames[nC] = Point(
                        (center.x + (radius
                                * 1.1
                                * Math.sin(
                            Math.toRadians(orientation.toDouble())
                                    + flames[nC]
                        ))).toInt(), (center.y - (radius
                                * 1.1
                                * Math.cos(
                            Math.toRadians(orientation.toDouble())
                                    + flames[nC]
                        ))).toInt()
                    )
                } //end even/odd else
            } //end for loop


            g.color = colShip //flames same color as ship
            g.fillPolygon(
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
        draw(g, colShip)

    } //end draw()


}
