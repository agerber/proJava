package edu.uchicago.gerber._08final.mvc.model

import java.awt.Graphics
import java.awt.Point

interface Movable {
    enum class Team {
        FRIEND, FOE, FLOATER, DEBRIS
    }

    //for the game to move and draw movable objects
    fun move()
    fun draw(g: Graphics?)

    //for collision detection
    fun myCenter(): Point
    fun myRadius(): Int
    fun myTeam(): Team
    fun isProtected(): Boolean
} //end Movable
