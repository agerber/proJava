package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.model.Movable.Team
import java.awt.Color
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
        val pntCs = ArrayList<Point>()
        pntCs.add(Point(5, 5))
        pntCs.add(Point(4, 0))
        pntCs.add(Point(5, -5))
        pntCs.add(Point(0, -4))
        pntCs.add(Point(-5, -5))
        pntCs.add(Point(-4, 0))
        pntCs.add(Point(-5, 5))
        pntCs.add(Point(0, 4))
        cartesians = pntCs
    }


}
