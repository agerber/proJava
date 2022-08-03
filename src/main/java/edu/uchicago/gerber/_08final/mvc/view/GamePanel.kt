package edu.uchicago.gerber._08final.mvc.view

import edu.uchicago.gerber._08final.mvc.controller.Game
import edu.uchicago.gerber._08final.mvc.model.CommandCenter
import edu.uchicago.gerber._08final.mvc.model.Movable
import java.awt.*
import java.util.*
import java.util.function.BiConsumer

class GamePanel(dim: Dimension?) : Panel() {
    // ==============================================================
    // FIELDS 
    // ============================================================== 
    // The following "off" vars are used for the off-screen double-buffered image.
    private var imgOff: Image? = null
    private var grpOff: Graphics? = null
    private val gmf: GameFrame
    private val fnt = Font("SansSerif", Font.BOLD, 12)
    private val fntBig = Font("SansSerif", Font.BOLD + Font.ITALIC, 36)
    private var fmt: FontMetrics? = null
    private var fontWidth = 0
    private var fontHeight = 0
    private var strDisplay = ""

    // ==============================================================
    // CONSTRUCTOR 
    // ==============================================================
    init {
        gmf = GameFrame()
        gmf.contentPane.add(this)
        gmf.pack()
        initView()
        gmf.size = dim
        gmf.title = "Game Base"
        gmf.isResizable = false
        gmf.isVisible = true
        isFocusable = true
    }

    // ==============================================================
    // METHODS 
    // ==============================================================
    private fun drawScore(g: Graphics?) {
        g!!.color = Color.white
        g.font = fnt
        if (CommandCenter.score != 0L) {
            g.drawString("SCORE :  " + CommandCenter.score, fontWidth, fontHeight)
        } else {
            g.drawString("NO SCORE", fontWidth, fontHeight)
        }
    }

    override fun update(g: Graphics) {
        //create an image off-screen
        imgOff = createImage(Game.DIM.width, Game.DIM.height)
        //get its graphics context
        grpOff = imgOff!!.getGraphics()

        //Fill the off-screen image background with black.
        grpOff!!.setColor(Color.black)
        grpOff!!.fillRect(0, 0, Game.DIM.width, Game.DIM.height)
        drawScore(grpOff)
        if (CommandCenter.isGameOver) {
            displayTextOnScreen()
        } else if (CommandCenter.paused) {
            strDisplay = "Game Paused"
            grpOff!!.drawString(strDisplay,
                    (Game.DIM.width - fmt!!.stringWidth(strDisplay)) / 2, Game.DIM.height / 4)
        } else {
            iterateMovables(grpOff,
                    CommandCenter.movDebris,
                    CommandCenter.movFloaters,
                    CommandCenter.movFoes,
                    CommandCenter.movFriends)
            drawNumberShipsLeft(grpOff)
        }

        //after drawing all the movables or text on the offscreen-image, copy it in one fell-swoop to graphics context
        // of the game panel, and show it for ~40ms. If you attempt to draw sprites directly on the gamePanel, e.g.
        // without the use of a double-buffered off-screen image, you will see flickering.
        g.drawImage(imgOff, 0, 0, this)
    }

    @SafeVarargs
    private fun iterateMovables(g: Graphics?, vararg arrayOfListMovables: List<Movable>) {
        val moveDraw = BiConsumer { grp: Graphics?, mov: Movable ->
            mov.move()
            mov.draw(grp)
        }

        //we use flatMap to flatten the List<Movable>[] passed-in above into a single stream of Movables
        Arrays.stream(arrayOfListMovables) //Stream<List<Movable>>
                .flatMap { obj: List<Movable> -> obj.stream() } //Stream<Movable>
                .forEach { m: Movable -> moveDraw.accept(g, m) }
    }

    private fun drawNumberShipsLeft(g: Graphics?) {
        var numFalcons = CommandCenter.numFalcons
        while (numFalcons > 0) {
            //drawOneShipLeft(g, numFalcons--)
        }
    }

    // Draw the number of falcons left on the bottom-right of the screen. Upside-down, but ok.
    private fun drawOneShipLeft(g: Graphics?, offSet: Int) {
        val falcon = CommandCenter.falcon
        g!!.color = falcon.color
        g.drawPolygon(
                Arrays.stream(falcon.cartesians.toTypedArray())
                        .map { pnt: Point -> pnt.x + Game.DIM.width - 20 * offSet }
                        .mapToInt { obj: Int -> obj }
                        .toArray(),
                Arrays.stream(falcon.cartesians.toTypedArray())
                        .map { pnt: Point -> pnt.y + Game.DIM.height - 40 }
                        .mapToInt { obj: Int -> obj }
                        .toArray(),
                falcon.cartesians.size)
    }

    private fun initView() {
        val g = graphics // get the graphics context for the panel
        g.font = fnt // take care of some simple font stuff
        fmt = g.fontMetrics
        fontWidth = fmt!!.getMaxAdvance()
        fontHeight = fmt!!.getHeight()
        g.font = fntBig // set font info
    }

    // This method draws some text to the middle of the screen before/after a game
    private fun displayTextOnScreen() {
        strDisplay = "GAME OVER"
        grpOff!!.drawString(strDisplay,
                (Game.DIM.width - fmt!!.stringWidth(strDisplay)) / 2, Game.DIM.height / 4)
        strDisplay = "use the arrow keys to turn and thrust"
        grpOff!!.drawString(strDisplay,
                (Game.DIM.width - fmt!!.stringWidth(strDisplay)) / 2, Game.DIM.height / 4 + fontHeight + 40)
        strDisplay = "use the space bar to fire"
        grpOff!!.drawString(strDisplay,
                (Game.DIM.width - fmt!!.stringWidth(strDisplay)) / 2, Game.DIM.height / 4 + fontHeight + 80)
        strDisplay = "'S' to Start"
        grpOff!!.drawString(strDisplay,
                (Game.DIM.width - fmt!!.stringWidth(strDisplay)) / 2, Game.DIM.height / 4 + fontHeight + 120)
        strDisplay = "'P' to Pause"
        grpOff!!.drawString(strDisplay,
                (Game.DIM.width - fmt!!.stringWidth(strDisplay)) / 2, Game.DIM.height / 4 + fontHeight + 160)
        strDisplay = "'Q' to Quit"
        grpOff!!.drawString(strDisplay,
                (Game.DIM.width - fmt!!.stringWidth(strDisplay)) / 2, Game.DIM.height / 4 + fontHeight + 200)
        strDisplay = "left pinkie on 'A' for Shield"
        grpOff!!.drawString(strDisplay,
                (Game.DIM.width - fmt!!.stringWidth(strDisplay)) / 2, Game.DIM.height / 4 + fontHeight + 240)
        strDisplay = "'Numeric-Enter' for Hyperspace"
        grpOff!!.drawString(strDisplay,
                (Game.DIM.width - fmt!!.stringWidth(strDisplay)) / 2, Game.DIM.height / 4 + fontHeight + 280)
    }
}
