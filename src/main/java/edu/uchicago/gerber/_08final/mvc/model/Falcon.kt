package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.model.Movable.Team
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage


 class Falcon : Sprite() {
    // ==============================================================
    // FIELDS 
    // ==============================================================
    companion object {
        private const val THRUST = 0.85
        private const val DEGREE_STEP = 9
        const val SPAWN_INIT_VALUE = 68
        const val MAX_SHIELD = 200
        //image states
        const val FALCON = 0 //normal ship
        const val FALCON_THR = 1 //normal ship thrusting
        const val FALCON_PRO = 2 //protected ship (green)
        const val FALCON_PRO_THR = 3 //protected ship (green) thrusting
        const val FALCON_INVISIBLE = 4 //for pre-spawning
    }

    var thrusting = false
    var shield = 0
    var invisible = 0
    var showLevel = 0

    enum class TurnState {
        IDLE, LEFT, RIGHT
    }

    var turnState = TurnState.IDLE




     // ==============================================================
    // CONSTRUCTOR 
    // ==============================================================
    init {
        team = Team.FRIEND
        //this is the size (radius) of the falcon
        radius = 32



        val rasterMap: MutableMap<Int, BufferedImage?> = HashMap()
        rasterMap[FALCON] = loadGraphic("/imgs/fal/falcon125.png")
        rasterMap[FALCON_THR] = loadGraphic("/imgs/fal/falcon125_thr.png")
        rasterMap[FALCON_PRO] = loadGraphic("/imgs/fal/falcon125_PRO.png")
        rasterMap[FALCON_PRO_THR] = loadGraphic("/imgs/fal/falcon125_PRO_thr.png")
        rasterMap[FALCON_INVISIBLE] = null
        this.rasterMap = rasterMap

    }

    //if fading, then make invincible
    override fun isProtected(): Boolean {
        return shield > 0

    }

    // ==============================================================
    // METHODS 
    // ==============================================================
    override fun move() {
        super.move()
        if (invisible > 0) invisible--
        if (shield > 0) shield--
        //The falcon is a convenient place to decrement this variable as the falcon
        //move() method is being called every frame (~40ms); and the falcon reference is never null.
        //The falcon is a convenient place to decrement this variable as the falcon
        //move() method is being called every frame (~40ms); and the falcon reference is never null.
        if (showLevel > 0) showLevel--

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

    }



    //raster implementation of draw()
    override fun draw(g: Graphics) {

        //set image-state
        val imageState: Int
        if (invisible > 0){
            imageState = FALCON_INVISIBLE
        }
        else if (isProtected()) {
          imageState =  if (thrusting) FALCON_PRO_THR else FALCON_PRO
            //you can also combine vector elements and raster elements
            drawShield(g)
        } else { //not protected
            imageState =   if (thrusting) FALCON_THR else FALCON
        }

        //cast (widen the aperture of) the graphics object to gain access to methods of Graphics2D
        //and render the image according to the image-state
        renderRaster((g as Graphics2D), rasterMap[imageState])


    }

     private fun drawShield(g: Graphics) {
         g.color = Color.CYAN
         g.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2)
     }


}
