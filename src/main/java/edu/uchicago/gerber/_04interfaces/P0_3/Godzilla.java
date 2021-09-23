

package edu.uchicago.gerber._04interfaces.P0_3;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ag
 * Date: 9/18/13
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Godzilla implements Raceable {


    private int mStride;
    private final int FEET = 15;     //2
    private final double mSpeed = .05;   //percentage  //.80
    private Random mRandom;
    private String mAscii;


    public Godzilla() {
        mStride = 0;
        mRandom = new Random();
        setAscii(FileOps.convertFileToString("//src//P0_3//godzilla.txt"));
    }

    @Override
    public void stride() {

        if (mRandom.nextInt(100) * mSpeed > 4) {
            mStride += FEET;
        }

        System.out.print("Godzilla >");
        for (int nC = 0; nC < mStride; nC++) {
            System.out.print("@");
        }
        System.out.println();
    }

    @Override
    public boolean isFinished() {
        return  mStride >= FINISH;
    }

    @Override
    public String exclaimVictory() {
        return "Tokyo is destroyed! GGGRRRRRRRRAAAAA.\n\n";

    }

    @Override
    public String toString() {
      return  getAscii();

    }

    @Override
    public void reset() {
        mStride = 0;
    }

    public String getAscii() {
        return mAscii;
    }

    public void setAscii(String ascii) {
        mAscii = ascii;
    }
}




