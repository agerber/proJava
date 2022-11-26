package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.model.Movable.Team
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.util.*


class Falcon : Sprite() {
    // ==============================================================
    // FIELDS 
    // ==============================================================
    companion object {
        private const val THRUST = .65
        private const val DEGREE_STEP = 9
        const val SPAWN_INIT_VALUE = 68
    }

    var thrusting = false
    var spawn = 0

    enum class TurnState {
        IDLE, LEFT, RIGHT
    }

    var turnState = TurnState.IDLE

    enum class ImageState {
        FALCON,  //normal ship
        FALCON_THR,  //normal ship thrusting
        FALCON_PRO,  //protected ship (green)
        FALCON_PRO_THR //protected ship (green) thrusting
    }


    // ==============================================================
    // CONSTRUCTOR 
    // ==============================================================
    init {
        team = Team.FRIEND
        //this is the size (radius) of the falcon
        radius = 32



        val rasterMap: MutableMap<String, BufferedImage?> = HashMap()
        rasterMap[ImageState.FALCON.toString()] = loadGraphic("/imgs/falcon50.png")
        rasterMap[ImageState.FALCON_THR.toString()] = loadGraphic("/imgs/falcon50thrust.png")
        rasterMap[ImageState.FALCON_PRO.toString()] = loadGraphic("/imgs/falcon50protect.png")
        rasterMap[ImageState.FALCON_PRO_THR.toString()] = loadGraphic("/imgs/falcon50protect_thrust.png")
        this.rasterMap = rasterMap

    }

    //if fading, then make invincible
    override fun isProtected(): Boolean {
        return spawn > 0

    }

    // ==============================================================
    // METHODS 
    // ==============================================================
    override fun move() {
        super.move()
        if (spawn > 0) spawn--

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


    //raster implementation of draw()
    @Override
    override fun draw(g: Graphics) {

        //set image-state
        val imageState: ImageState
        imageState = if (isProtected()) {
            if (thrusting) ImageState.FALCON_PRO_THR else ImageState.FALCON_PRO
        } else { //not protected
            if (thrusting) ImageState.FALCON_THR else ImageState.FALCON
        }

        //cast (widen the aperture of) the graphics object to gain access to methods of Graphics2D
        //and render the image according to the image-state
        renderRaster((g as Graphics2D), rasterMap[imageState.toString()]!!)

        //draw cyan shield, and warn player of impending non-protection
        if (isProtected() && !(spawn <= 21 && spawn % 7 == 0)) {
            //you can add vector elements to raster graphics
            g.setColor(Color.CYAN)
            g.drawOval(center.x - radius, center.y - radius, radius * 2, radius * 2)
        }
    }


}
