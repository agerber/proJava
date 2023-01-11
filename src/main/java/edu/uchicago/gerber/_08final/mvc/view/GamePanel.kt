package edu.uchicago.gerber._08final.mvc.view

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter
import edu.uchicago.gerber._08final.mvc.controller.Game
import edu.uchicago.gerber._08final.mvc.controller.Utils
import edu.uchicago.gerber._08final.mvc.model.Movable
import edu.uchicago.gerber._08final.mvc.model.PolarPoint
import java.awt.*
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.BiConsumer
import java.util.function.Function


class GamePanel(dim: Dimension?) : Panel() {
    // ==============================================================
    // FIELDS 
    // ============================================================== 
    // The following "off" vars are used for the off-screen double-buffered image.
    private var imgOff: Image
    private var grpOff: Graphics
    private val gmf: GameFrame
    private val fnt: Font
    private val fntBig: Font
    private var fmt: FontMetrics

    private var fontWidth = 0
    private var fontHeight = 0
    private var strDisplay = ""

    private val pntShip: Array<Point>

    // ==============================================================
    // CONSTRUCTOR 
    // ==============================================================
    init {
        gmf = GameFrame()
        gmf.contentPane.add(this)
        //clone it

        // Robert Alef's awesome falcon design
        val listShip: MutableList<Point> = ArrayList()
        listShip.add(Point(0, 9))
        listShip.add(Point(-1, 6))
        listShip.add(Point(-1, 3))
        listShip.add(Point(-4, 1))
        listShip.add(Point(4, 1))
        listShip.add(Point(-4, 1))
        listShip.add(Point(-4, -2))
        listShip.add(Point(-1, -2))
        listShip.add(Point(-1, -9))
        listShip.add(Point(-1, -2))
        listShip.add(Point(-4, -2))
        listShip.add(Point(-10, -8))
        listShip.add(Point(-5, -9))
        listShip.add(Point(-7, -11))
        listShip.add(Point(-4, -11))
        listShip.add(Point(-2, -9))
        listShip.add(Point(-2, -10))
        listShip.add(Point(-1, -10))
        listShip.add(Point(-1, -9))
        listShip.add(Point(1, -9))
        listShip.add(Point(1, -10))
        listShip.add(Point(2, -10))
        listShip.add(Point(2, -9))
        listShip.add(Point(4, -11))
        listShip.add(Point(7, -11))
        listShip.add(Point(5, -9))
        listShip.add(Point(10, -8))
        listShip.add(Point(4, -2))
        listShip.add(Point(1, -2))
        listShip.add(Point(1, -9))
        listShip.add(Point(1, -2))
        listShip.add(Point(4, -2))
        listShip.add(Point(4, 1))
        listShip.add(Point(1, 3))
        listShip.add(Point(1, 6))
        listShip.add(Point(0, 9))

        //this just displays the ships remaining
        pntShip  = Utils.pointsListToArray(listShip)


        gmf.pack()
        initView()
        gmf.size = dim
        gmf.title = "Game Base"
        gmf.isResizable = false
        gmf.isVisible = true
        isFocusable = true

        imgOff = createImage(Game.DIM.width, Game.DIM.height)
        grpOff = imgOff.getGraphics()
        fnt = Font("SansSerif", Font.BOLD, 12)
        fntBig = Font("SansSerif", Font.BOLD + Font.ITALIC, 36)
        // val g = graphics // get the graphics context for the panel
        graphics.font = fnt // take care of some simple font stuff
        fmt = graphics.fontMetrics
    }

    // ==============================================================
    // METHODS 
    // ==============================================================
    private fun drawScore(g: Graphics) {
        g.color = Color.white
        g.font = fnt
        if (CommandCenter.score != 0L) {
            g.drawString("SCORE :  " + CommandCenter.score, fontWidth, fontHeight)
        } else {
            g.drawString("NO SCORE", fontWidth, fontHeight)
        }
    }

    private fun drawLevel(graphics: Graphics) {
        val levelText = "Level: " + CommandCenter.level
        graphics.drawString(levelText, 20, 30) //upper-left corner
        if (CommandCenter.falcon.showLevel > 0) {
            displayTextOnScreen(graphics, levelText) //middle of the screen
        }
    }

    //this is used for development, you can remove it from your final game
    private fun drawFrame(g: Graphics) {
        g.color = Color.white
        g.font = fnt
        g.drawString("FRAME :  " + CommandCenter.frame, fontWidth, Game.DIM.height - (fontHeight + 22))

    }

    override fun update(g: Graphics) {
        //create an image off-screen
        imgOff = createImage(Game.DIM.width, Game.DIM.height)
        //get its graphics context
        grpOff = imgOff.getGraphics()

        //Fill the off-screen image background with black.
        grpOff.setColor(Color.black)
        grpOff.fillRect(0, 0, Game.DIM.width, Game.DIM.height)
        drawScore(grpOff)
        drawFrame(grpOff)
        if (CommandCenter.isGameOver()) {
            displayTextOnScreen(
                grpOff,
                "GAME OVER",
                "use the arrow keys to turn and thrust",
                "use the space bar to fire",
                "'S' to Start",
                "'P' to Pause",
                "'Q' to Quit",
                "'M' to toggle music"
            )

        } else if (CommandCenter.paused) {
            strDisplay = "Game Paused"
            grpOff.drawString(
                strDisplay,
                (Game.DIM.width - fmt.stringWidth(strDisplay)) / 2, Game.DIM.height / 4
            )
        } else {
            processMovables(
                grpOff,
                CommandCenter.movDebris,
                CommandCenter.movFloaters,
                CommandCenter.movFoes,
                CommandCenter.movFriends
            )
            drawNumberShipsLeft(grpOff)
            drawShieldMeter(grpOff)
            drawScore(grpOff)
            drawLevel(grpOff)
            drawNumFrame(grpOff)

        }

        //after drawing all the movables or text on the offscreen-image, copy it in one fell-swoop to graphics context
        // of the game panel, and show it for ~40ms. If you attempt to draw sprites directly on the gamePanel, e.g.
        // without the use of a double-buffered off-screen image, you will see flickering.
        g.drawImage(imgOff, 0, 0, this)
    }

    //this method causes all sprites to move and draw themselves
    @SafeVarargs
    private fun processMovables(g: Graphics, vararg arrayOfListMovables: List<Movable>) {

        val moveDraw = BiConsumer { grp: Graphics, mov: Movable ->
            mov.move()
            mov.draw(grp)
        }

        //we use flatMap to flatten the List<Movable>[] passed-in above into a single stream of Movables
        Arrays.stream(arrayOfListMovables) //Stream<List<Movable>>
            .flatMap { obj: List<Movable> -> obj.stream() } //Stream<Movable>
            .forEach { m: Movable -> moveDraw.accept(g, m) }
    }

    private fun drawNumberShipsLeft(g: Graphics) {
        var numFalcons = CommandCenter.numFalcons
        while (numFalcons > 0) {
            drawOneShipLeft(g, numFalcons--)
        }
    }

    private fun drawNumFrame(g: Graphics) {
        g.color = Color.white
        g.font = fnt
        g.drawString(
            "FRAME :  " + CommandCenter.frame, fontWidth,
            Game.DIM.height - (fontHeight + 22)
        )
    }

    private fun drawShieldMeter(g: Graphics) {
        val shieldMeter: Int = CommandCenter.falcon.shield / 2
        g.color = Color.CYAN
        g.fillRect(Game.DIM.width - 220, Game.DIM.height - 45, shieldMeter, 10)
        g.color = Color.DARK_GRAY
        g.drawRect(Game.DIM.width - 220, Game.DIM.height - 45, 100, 10)
    }

    // Draw the number of falcons left on the bottom-right of the screen.
    private fun drawOneShipLeft(g: Graphics, offSet: Int) {

        g.color = Color.ORANGE

        val SIZE = 15
        val DEGREES = 90.0
        val X_POS = 27
        val Y_POS = 45

        val rotateFalcon90 = Function { pp: PolarPoint ->
            Point(
                (pp.r * SIZE
                        * Math.sin(
                    Math.toRadians(DEGREES)
                            + pp.theta
                )).toInt(),
                (pp.r * SIZE
                        * Math.cos(
                    Math.toRadians(DEGREES)
                            + pp.theta
                )).toInt()
            )
        }

        g.drawPolygon(
           Arrays.stream( Utils.cartesianToPolar(pntShip))
                .map(rotateFalcon90)
                .map { pnt: Point -> pnt.x + Game.DIM.width - X_POS * offSet }
                .mapToInt { obj: Int -> obj }
                .toArray(),
            Arrays.stream( Utils.cartesianToPolar(pntShip))
                .map(rotateFalcon90)
                .map { pnt: Point -> pnt.y + Game.DIM.height - Y_POS }
                .mapToInt { obj: Int -> obj }
                .toArray(),
            pntShip.size)
    }

    private fun initView() {
        val g = graphics // get the graphics context for the panel
        g.font = fnt // take care of some simple font stuff
        fmt = g.fontMetrics
        fontWidth = fmt.getMaxAdvance()
        fontHeight = fmt.getHeight()
        g.font = fntBig // set font info
    }

    private fun displayTextOnScreen(graphics: Graphics, vararg lines: String) {

        val spacer = AtomicInteger(0)
        Arrays.stream(lines)
            .forEach { s: String ->
                graphics.drawString(
                    s, (Game.DIM.width - fmt.stringWidth(s)) / 2,
                    Game.DIM.height / 4 + fontHeight + spacer.getAndAdd(40)
                )
            }
    }


}
