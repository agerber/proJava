package edu.uchicago.gerber._08final.mvc.view;

import edu.uchicago.gerber._08final.mvc.controller.CommandCenter;
import edu.uchicago.gerber._08final.mvc.controller.Game;
import edu.uchicago.gerber._08final.mvc.controller.Utils;
import edu.uchicago.gerber._08final.mvc.model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;


public class GamePanel extends Panel {

    // ==============================================================
    // FIELDS
    // ==============================================================
    private final Font fontNormal = new Font("SansSerif", Font.BOLD, 12);
    private final Font fontBig = new Font("SansSerif", Font.BOLD + Font.ITALIC, 36);
    private FontMetrics fontMetrics;
    private int fontWidth;
    private int fontHeight;

    //used to draw number of ships remaining
    private final Point[] pntShipsRemaining;


    // ==============================================================
    // CONSTRUCTOR
    // ==============================================================

    public GamePanel(Dimension dim) {
        GameFrame gameFrame = new GameFrame();
        gameFrame.getContentPane().add(this);

        // Robert Alef's awesome falcon design
        List<Point> listShip = new ArrayList<>();
        listShip.add(new Point(0,9));
        listShip.add(new Point(-1, 6));
        listShip.add(new Point(-1,3));
        listShip.add(new Point(-4, 1));
        listShip.add(new Point(4,1));
        listShip.add(new Point(-4,1));
        listShip.add(new Point(-4, -2));
        listShip.add(new Point(-1, -2));
        listShip.add(new Point(-1, -9));
        listShip.add(new Point(-1, -2));
        listShip.add(new Point(-4, -2));
        listShip.add(new Point(-10, -8));
        listShip.add(new Point(-5, -9));
        listShip.add(new Point(-7, -11));
        listShip.add(new Point(-4, -11));
        listShip.add(new Point(-2, -9));
        listShip.add(new Point(-2, -10));
        listShip.add(new Point(-1, -10));
        listShip.add(new Point(-1, -9));
        listShip.add(new Point(1, -9));
        listShip.add(new Point(1, -10));
        listShip.add(new Point(2, -10));
        listShip.add(new Point(2, -9));
        listShip.add(new Point(4, -11));
        listShip.add(new Point(7, -11));
        listShip.add(new Point(5, -9));
        listShip.add(new Point(10, -8));
        listShip.add(new Point(4, -2));
        listShip.add(new Point(1, -2));
        listShip.add(new Point(1, -9));
        listShip.add(new Point(1, -2));
        listShip.add(new Point(4,-2));
        listShip.add(new Point(4, 1));
        listShip.add(new Point(1, 3));
        listShip.add(new Point(1,6));
        listShip.add(new Point(0,9));

        pntShipsRemaining = listShip.toArray(new Point[0]);

        gameFrame.pack();
        initView();
        gameFrame.setSize(dim);
        //change the name of the game-frame to your game name
        gameFrame.setTitle("Game Base");
        gameFrame.setResizable(false);
        gameFrame.setVisible(true);
        setFocusable(true);
    }


    // ==============================================================
    // METHODS
    // ==============================================================

    private void drawScore(Graphics g) {
        g.setColor(Color.white);
        g.setFont(fontNormal);
        if (CommandCenter.getInstance().getScore() > 0) {
            g.drawString("Score :  " + CommandCenter.getInstance().getScore(), fontWidth, fontHeight);
        }
    }
    //this is used for development, you can remove it from your final game
    private void drawNumFrame(Graphics g) {
        g.setColor(Color.white);
        g.setFont(fontNormal);
        g.drawString("FRAME :  " + CommandCenter.getInstance().getFrame(), fontWidth,
                Game.DIM.height  - (fontHeight + 22));

    }

    private void drawShieldMeter(Graphics g){

        int shieldMeter =   CommandCenter.getInstance().getFalcon().getShield() / 2;

        g.setColor(Color.CYAN);
        g.fillRect(Game.DIM.width - 220, Game.DIM.height -45, shieldMeter, 10);

        g.setColor(Color.DARK_GRAY);
        g.drawRect(Game.DIM.width - 220, Game.DIM.height -45, 100, 10);
    }


    public void update(Graphics g) {
        //create an image off-screen
        // The following "off" vars are used for the off-screen double-buffered image.
        Image imgOff = createImage(Game.DIM.width, Game.DIM.height);
        //get its graphics context
        Graphics grpOff = imgOff.getGraphics();

        //Fill the off-screen image background with black.
        grpOff.setColor(Color.BLACK);
        grpOff.fillRect(0, 0, Game.DIM.width, Game.DIM.height);

        //this is used for development, you may remove drawNumFrame() in your final game.
        drawNumFrame(grpOff);

        if (CommandCenter.getInstance().isGameOver()) {
            displayTextOnScreen(grpOff,
                    "GAME OVER",
                    "use the arrow keys to turn and thrust",
                    "use the space bar to fire",
                    "'S' to Start",
                    "'P' to Pause",
                    "'Q' to Quit",
                    "'M' to toggle music"

            );
        } else if (CommandCenter.getInstance().isPaused()) {

            displayTextOnScreen(grpOff, "Game Paused");

        }

        //playing and not paused!
        else {


            moveDrawMovables(grpOff,
                    CommandCenter.getInstance().getMovDebris(),
                    CommandCenter.getInstance().getMovFloaters(),
                    CommandCenter.getInstance().getMovFoes(),
                    CommandCenter.getInstance().getMovFriends());


            drawNumberShipsRemaining(grpOff);
            drawShieldMeter(grpOff);
            drawScore(grpOff);
            drawLevel(grpOff);


        }

        //after drawing all the movables or text on the offscreen-image, copy it in one fell-swoop to graphics context
        // of the game panel, and show it for ~40ms. If you attempt to draw sprites directly on the gamePanel, e.g.
        // without the use of a double-buffered off-screen image, you will see flickering.
        g.drawImage(imgOff, 0, 0, this);
    }


    //this method causes all sprites to move and draw themselves
    @SafeVarargs
    private final void moveDrawMovables(final Graphics g, List<Movable>... arrayOfListMovables) {

        BiConsumer<Graphics, Movable> moveDraw = (grp, mov) -> {
            mov.move();
            mov.draw(grp);
        };

        //we use flatMap to flatten the List<Movable>[] passed-in above into a single stream of Movables
        Arrays.stream(arrayOfListMovables) //Stream<List<Movable>>
                .flatMap(Collection::stream) //Stream<Movable>
                .forEach(m -> moveDraw.accept(g, m));


    }

    private void drawLevel(final Graphics graphics){
        final String levelText = "Level: " + CommandCenter.getInstance().getLevel();
        graphics.drawString(levelText, 20, 30); //upper-left corner
        if (CommandCenter.getInstance().getFalcon().getShowLevel() > 0) {
            displayTextOnScreen(graphics, levelText); //middle of the screen
        }
    }


    // Draw the number of falcons remaining on the bottom-right of the screen.
    private void drawNumberShipsRemaining(Graphics g) {
        int numFalcons = CommandCenter.getInstance().getNumFalcons();
        while (numFalcons > 0) {
            drawOneShip(g, numFalcons--);
        }
    }


    private void drawOneShip(Graphics g, int offSet) {

        g.setColor(Color.ORANGE);

        //rotate the ship 90 degrees
        final double DEGREES_90 = 90.0;
        final int RADIUS = 15;
        final int X_POS = Game.DIM.width - (27 * offSet);
        final int Y_POS = Game.DIM.height - 45;

        //the reason we convert to polar-points is that it's much easier to rotate polar-points.
        List<PolarPoint> polars = Utils.cartesianToPolar(pntShipsRemaining);

        Function<PolarPoint, PolarPoint> rotatePolarBy90 =
                pp -> new PolarPoint(
                        pp.getR(),
                        pp.getTheta() + Math.toRadians(DEGREES_90) //rotated Theta
                );

        Function<PolarPoint, Point> polarToCartesian =
                pp -> new Point(
                        (int)  (pp.getR() * RADIUS * Math.sin(pp.getTheta())),
                        (int)  (pp.getR() * RADIUS * Math.cos(pp.getTheta())));

        Function<Point, Point> adjustForLocation =
                pnt -> new Point(
                        pnt.x + X_POS,
                        pnt.y + Y_POS);


        g.drawPolygon(

                polars.stream()
                        .map(rotatePolarBy90)
                        .map(polarToCartesian)
                        .map(adjustForLocation)
                        .map(pnt -> pnt.x)
                        .mapToInt(Integer::intValue)
                        .toArray(),

                polars.stream()
                        .map(rotatePolarBy90)
                        .map(polarToCartesian)
                        .map(adjustForLocation)
                        .map(pnt -> pnt.y)
                        .mapToInt(Integer::intValue)
                        .toArray(),

                polars.size());


    }

    private void initView() {
        Graphics g = getGraphics();            // get the graphics context for the panel
        g.setFont(fontNormal);                        // take care of some simple font stuff
        fontMetrics = g.getFontMetrics();
        fontWidth = fontMetrics.getMaxAdvance();
        fontHeight = fontMetrics.getHeight();
        g.setFont(fontBig);                    // set font info
    }


    // This method draws some text to the middle of the screen
    private void displayTextOnScreen(final Graphics graphics, String... lines) {

        //AtomicInteger is safe to pass into a stream
        final AtomicInteger spacer = new AtomicInteger(0);
        Arrays.stream(lines)
                .forEach(s ->
                            graphics.drawString(s, (Game.DIM.width - fontMetrics.stringWidth(s)) / 2,
                                    Game.DIM.height / 4 + fontHeight + spacer.getAndAdd(40))

                );


    }


}
