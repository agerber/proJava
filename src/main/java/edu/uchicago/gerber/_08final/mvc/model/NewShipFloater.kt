package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.controller.Utils
import edu.uchicago.gerber._08final.mvc.model.Movable.Team
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.util.*

class NewShipFloater : Sprite() {
    init {
        team = Team.FLOATER
        expiry = 251
        radius = 50
        color = Color.BLUE

        //set random DeltaX
        deltaX = somePosNegValue(10).toDouble()

        //set rnadom DeltaY
        deltaY = somePosNegValue(10).toDouble()

        //set random spin
        spin = somePosNegValue(10)

        //cartesian points which define the shape of the polygon
        val listPoint = ArrayList<Point>()
        listPoint.add(Point(5, 5))
        listPoint.add(Point(4, 0))
        listPoint.add(Point(5, -5))
        listPoint.add(Point(0, -4))
        listPoint.add(Point(-5, -5))
        listPoint.add(Point(-4, 0))
        listPoint.add(Point(-5, 5))
        listPoint.add(Point(0, 4))
        cartesians = Utils.pointsListToArray(listPoint)
    }

    override fun draw(g: Graphics) {
        renderVector(g)
    }


}
