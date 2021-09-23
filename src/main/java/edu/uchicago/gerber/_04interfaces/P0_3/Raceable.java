package edu.uchicago.gerber._04interfaces.P0_3;

/**
 * Created with IntelliJ IDEA.
 * User: ag
 * Date: 9/18/13
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Raceable {

    public static final int FINISH = 100;

    public void stride();
    public boolean isFinished();
    public String exclaimVictory();
    public void reset();


}
