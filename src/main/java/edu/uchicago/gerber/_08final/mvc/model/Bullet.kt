package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.model.Movable.Team
import java.awt.Point

class Bullet(fal: Falcon) : Sprite() {
    init {
        team = Team.FRIEND

        //a bullet expires after 20 frames. set to one more than frame expiration
        expiry = 21
        radius = 6


        //everything is relative to the falcon ship that fired the bullet
        center = fal.center

        //set the bullet orientation to the falcon (ship) orientation
        orientation = fal.orientation
        val FIRE_POWER = 35.0
        deltaX = fal.deltaX +
                Math.cos(Math.toRadians(fal.orientation.toDouble())) * FIRE_POWER
        deltaY = fal.deltaY +
                Math.sin(Math.toRadians(fal.orientation.toDouble())) * FIRE_POWER


        //defined the points on a cartesian grid
        val pntCs: MutableList<Point> = ArrayList()
        pntCs.add(Point(0, 3)) //top point
        pntCs.add(Point(1, -1))
        pntCs.add(Point(0, -2))
        pntCs.add(Point(-1, -1))
        setCartesians(pntCs)
    }
}