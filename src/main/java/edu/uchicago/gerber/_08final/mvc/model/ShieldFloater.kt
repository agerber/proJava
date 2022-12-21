package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.controller.Game
import java.awt.Color

class ShieldFloater : Floater() {
    init {
        color = Color.CYAN
        expiry = 260
    }

    companion object {
        //spawn every 25 seconds
        const val SPAWN_SHIELD_FLOATER = Game.FRAMES_PER_SECOND * 25
    }
}
