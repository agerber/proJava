package edu.uchicago.gerber._08final.mvc.model

import edu.uchicago.gerber._08final.mvc.controller.Game
import edu.uchicago.gerber._08final.mvc.model.Movable.Team
import lombok.Data
import lombok.experimental.Tolerate
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.util.*
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.IntFunction
import java.util.stream.Collectors

abstract class Sprite : Movable {

    var deltaX: Double = 0.0
    var deltaY: Double = 0.0

    //team
    lateinit var team: Team
    override fun getTeam(): Team {
        return team
    }
    fun setTeam(team: Team){
        this.team = team
    }

    //radius
    var radius: Int = 0
    override fun getRadius(): Int {
        return radius
    }
    fun setRadius(radius: Int){
        this.radius = radius
    }

    //center
    private var center: Point
    override fun getCenter(): Point {
        return center
    }
    fun setCenter(center: Point){
        this.center = center
    }

    var orientation: Int = 0
    var expiry: Int = 0
    val spin: Int = 0
    var fade: Int = 0
    val color: Color = Color.WHITE

    //will be initialized in subclass
    lateinit var cartesians: Array<Point>

    init {
        center = (Point(Game.R.nextInt(Game.DIM.width),
                Game.R.nextInt(Game.DIM.height)))
    }



    override fun move() {

        //The following code block just keeps the sprite inside the bounds of the frame.
        //To ensure this behavior among all sprites in your game, make sure to call super.move() in extending classes
        // where you need to override the move() method.
        val center: Point = getCenter()

        //right-bounds reached
        if (center.x > Game.DIM.width) {
            setCenter(Point(1, center.y))
            //left-bounds reached
        } else if (center.x < 0) {
            setCenter(Point(Game.DIM.width - 1, center.y))
            //bottom-bounds reached
        } else if (center.y > Game.DIM.height) {
            setCenter(Point(center.x, 1))
            //top-bounds reached
        } else if (center.y < 0) {
            setCenter(Point(center.x, Game.DIM.height - 1))
            //in-bounds
        } else {
            val newXPos: Double = center.x + deltaX
            val newYPos: Double = center.y + deltaY
            setCenter(Point(newXPos.toInt(), newYPos.toInt()))
        }

        //expire (decrement expiry) on short-lived objects only
        //the default value of expiry is zero, so this block will only apply to expiring sprites
        if (expiry > 0) expire()
    }



    private fun expire() {

        //if a short-lived sprite has an expiry of one, it commits suicide by enqueuing itself (this) onto the
        //opsList with an operation of REMOVE
        if (expiry == 1) {
            CommandCenter.opsQueue.enqueue(this, GameOp.Action.REMOVE)
        }
        //and then decrements in all cases
        expiry--
    }

    protected fun hypotFunction(dX: Double, dY: Double): Double {
        return Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dY, 2.0))
    }

    protected fun somePosNegValue(seed: Int): Int {
        var randomNumber = Game.R.nextInt(seed)
        if (randomNumber % 2 == 0) randomNumber = -randomNumber
        return randomNumber
    }

    //by default, sprites are not protected
    override  fun isProtected(): Boolean {
        return  false
    }

    //certain Sprites, such as Asteroid use this
    protected fun polarToCartesian(polPolars: List<PolarPoint>): Array<Point> {

        //when casting from double to int, we truncate and lose precision, so best to be generous with multiplier
        val PRECISION_MULTIPLIER = 1000
        val polarToCartTransform = Function { (r, theta): PolarPoint ->
            Point((getCenter().x + (r * getRadius() * PRECISION_MULTIPLIER
                    * Math.sin(Math.toRadians(orientation.toDouble())
                    + theta!!))).toInt(), (getCenter().y - (r * getRadius() * PRECISION_MULTIPLIER
                    * Math.cos(Math.toRadians(orientation.toDouble())
                    + theta))).toInt())
        }
        return polPolars.stream()
                .map(polarToCartTransform)
                .toArray()
    }

    protected fun cartesianToPolar(pntCartesians: List<Point>): List<PolarPoint> {
        val cartToPolarTransform = BiFunction { pnt: Point, hyp: Double ->
            PolarPoint( //this is r from PolarPoint(r,theta).
                    hypotFunction(pnt.x.toDouble(), pnt.y.toDouble()) / hyp,  //r is relative to the largestHypotenuse
                    //this is theta from PolarPoint(r,theta)
                    Math.toDegrees(Math.atan2(pnt.y.toDouble(), pnt.x.toDouble())) * Math.PI / 180
            )
        }


        //determine the largest hypotenuse
        var largestHypotenuse = 0.0
        for (pnt in pntCartesians) if (hypotFunction(pnt.x.toDouble(), pnt.y.toDouble()) > largestHypotenuse) largestHypotenuse = hypotFunction(pnt.x.toDouble(), pnt.y.toDouble())


        //we must make hypotenuse final to pass into a stream.
        val hyp = largestHypotenuse
        return pntCartesians.stream()
                .map { pnt: Point -> cartToPolarTransform.apply(pnt, hyp) }
                .collect(Collectors.toList())
    }

    override fun draw(g: Graphics?) {
        //set the native color of the sprite
        g!!.color = getColor()
        render(g)
    }

    fun draw(g: Graphics, color: Color?) {
        //set custom color
        g.color = color
        render(g)
    }

    private fun render(g: Graphics?) {

        // to render this Sprite, we need to, 1: convert raw cartesians to raw polars, 2: adjust polars
        // for orientation of sprite. Convert back to cartesians 3: adjust for center-point (location).
        // and 4: pass the cartesian-x and cartesian-y coords as arrays, along with length, to drawPolygon().

        //convert raw cartesians to raw polars
        val polars = cartesianToPolar(Arrays.asList(cartesians))

        //rotate raw polars given the orientation of the sprite. Then convert back to cartesians.
        val adjustForOrientation = Function { (r, theta): PolarPoint ->
            Point((r * getRadius()
                    * Math.sin(Math.toRadians(orientation.toDouble())
                    + theta!!)).toInt(), (r * getRadius()
                    * Math.cos(Math.toRadians(orientation.toDouble())
                    + theta)).toInt())
        }

        // adjust for the location (center-point) of the sprite.
        // the reason we subtract the y-value has to do with how Java plots the vertical axis for
        // graphics (from top to bottom)
        val adjustForLocation = Function { p: Point ->
            Point(
                    getCenter().x + p.x,
                    getCenter().y - p.y)
        }
        g!!.drawPolygon(
                polars.stream()
                        .map(adjustForOrientation)
                        .map(adjustForLocation)
                        .map { pnt: Point -> pnt.x }
                        .mapToInt { obj: Int -> obj }
                        .toArray(),
                polars.stream()
                        .map(adjustForOrientation)
                        .map(adjustForLocation)
                        .map { pnt: Point -> pnt.y }
                        .mapToInt { obj: Int -> obj }
                        .toArray(),
                getCartesians().size)

        //for debugging center-point. Feel free to remove these two lines.
        //#########################################
        g.color = Color.ORANGE
        g.fillOval(getCenter().x - 1, getCenter().y - 1, 2, 2)
        //g.drawOval(getCenter().x - getRadius(), getCenter().y - getRadius(), getRadius() *2, getRadius() *2);
        //#########################################
    }

    //in order to overload a lombok'ed method, we need to use the @Tolerate annotation
    //this overloaded method allows us to pass-in either a List<Point> or Point[] (lombok'ed method) to setCartesians()
    @Tolerate
    fun setCartesians(pntPs: List<Point?>) {
        setCartesians(pntPs.stream()
                .toArray(IntFunction<Array<Point?>> { _Dummy_.__Array__() }))
    }
}
