

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


    private int stride;
    private final int FEET = 15;     //2
    private final double speed = .05;   //percentage  //.80
    private Random random;
    private String ascii;


    public Godzilla() {
        stride = 0;
        random = new Random();
        setAscii(FileOps.convertFileToString("//src//P0_3//godzilla.txt"));
    }

    @Override
    public void stride() {

        if (random.nextInt(100) * speed > 4) {
            stride += FEET;
        }

        System.out.print("Godzilla >");
        for (int nC = 0; nC < stride; nC++) {
            System.out.print("@");
        }
        System.out.println();
    }

    @Override
    public boolean isFinished() {
        return  stride >= FINISH;
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
        stride = 0;
    }

    public String getAscii() {
        return ascii;
    }

    public void setAscii(String ascii) {
        this.ascii = ascii;
    }
}




