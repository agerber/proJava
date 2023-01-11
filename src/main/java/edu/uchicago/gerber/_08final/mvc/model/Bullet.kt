package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.controller.Utils
import edu.uchicago.gerber._08final.mvc.model.Movable.Team
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.util.*

class Bullet(fal: Falcon) : Sprite() {


    init {
        team = Team.FRIEND
        color = Color.ORANGE

        //a bullet expires after 20 frames. set to one more than frame expiration
        expiry = 21
        radius = 6

        //everything is relative to the falcon ship that fired the bullet
        center = fal.center

        //set the bullet orientation to the falcon (ship) orientation
        orientation = fal.orientation

        val FIRE_POWER = 35.0
        val vectorX = Math.cos(Math.toRadians(orientation.toDouble())) * FIRE_POWER
        val vectorY = Math.sin(Math.toRadians(orientation.toDouble())) * FIRE_POWER

        //fire force: falcon inertia + fire-vector
        deltaX = fal.deltaX + vectorX
        deltaY = fal.deltaY + vectorY


        //fire kick-back on the falcon: inertia - fire-vector / some arbitrary divisor
        val KICK_BACK_DIVISOR = 36.0
        fal.deltaX = fal.deltaX - vectorX / KICK_BACK_DIVISOR
        fal.deltaY = fal.deltaY - vectorY / KICK_BACK_DIVISOR

        //define the points on a cartesian grid
        val listPoint = ArrayList<Point>()
        listPoint.add(Point(0, 3)) //top point
        listPoint.add(Point(1, -1))
        listPoint.add(Point(0, -2))
        listPoint.add(Point(-1, -1))
        cartesians = Utils.pointsListToArray(listPoint)
    }

    override fun draw(g: Graphics) {
        //set the native color of the sprite
        renderVector(g)
    }

}
