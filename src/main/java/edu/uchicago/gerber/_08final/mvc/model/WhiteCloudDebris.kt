package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.model.Movable.Team
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class WhiteCloudDebris(explodingSprite: Sprite) : Sprite() {
    init {

        //DEBRIS means that this sprite is inert, and does not interact with other teams.
        team = Team.DEBRIS
        val rasterMap: MutableMap<Any, BufferedImage?> = HashMap()
        //see readme.txt file in the resources/imgs/exp directory for how I created these assets
        rasterMap[0] = loadGraphic("/imgs/exp/row-1-column-1.png")
        rasterMap[1] = loadGraphic("/imgs/exp/row-1-column-2.png")
        rasterMap[2] = loadGraphic("/imgs/exp/row-1-column-3.png")
        rasterMap[3] = loadGraphic("/imgs/exp/row-2-column-1.png")
        rasterMap[4] = loadGraphic("/imgs/exp/row-2-column-2.png")
        rasterMap[5] = loadGraphic("/imgs/exp/row-2-column-3.png")
        rasterMap[6] = loadGraphic("/imgs/exp/row-3-column-1.png")
        rasterMap[7] = loadGraphic("/imgs/exp/row-3-column-2.png")
        rasterMap[8] = loadGraphic("/imgs/exp/row-3-column-3.png")
        this.rasterMap = rasterMap

        //expire it out after it has done its animation.
        expiry = rasterMap.size

        //everything is relative to the exploding sprite
        spin = explodingSprite.spin
        center = explodingSprite.center
        deltaX = explodingSprite.deltaX
        deltaY = explodingSprite.deltaY
        radius = (explodingSprite.radius * 1.3).toInt()
    }

    //In this example, we are simply in-order traversing the rasterMap once.
    //However, we could also create a looping animation; think bird flapping over and over.
    //We can also create a hybrid of looping and image-state; think Mario
    //walking (looping), standing (suspended loop), jumping (one state), crouching (another state).
    //See Falcon class for example of image-state.
    override fun draw(g: Graphics) {

        //we already have a simple decrement-to-zero counter with expiry; see move() method of Sprite
        val index = rasterMap.size - expiry - 1
        renderRaster((g as Graphics2D), rasterMap[index])
    }
}
