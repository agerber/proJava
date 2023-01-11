package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.controller.Game
import java.awt.Color

class NewWallFloater : Floater() {

    companion object {
        //spawn every 40 seconds
        const val SPAWN_NEW_WALL_FLOATER = Game.FRAMES_PER_SECOND * 40
    }
    init {
        color = Color.ORANGE
        expiry = 230
    }


}
