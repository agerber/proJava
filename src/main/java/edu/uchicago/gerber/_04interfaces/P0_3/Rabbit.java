package edu.uchicago.gerber._04interfaces.P0_3;


import java.util.Random;


/**
 * Created with IntelliJ IDEA.
 * User: ag
 * Date: 9/18/13
 * Time: 9:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Rabbit implements Raceable {

    private int string;
    private final int FEET = 3;
    private final double speed = .81;
    private Random random;
    private String ascii;


    public Rabbit() {
        string = 0;
        random = new Random();
        setAscii(FileOps.convertFileToString("//src//P0_3//rabbit.txt"));
    }

    @Override
    public void stride() {


        if (random.nextInt(100) * speed > 4) {
            string += FEET;
        }
        System.out.print("Rabbit   >");
        for (int nC = 0; nC < string; nC++) {
            System.out.print("*");
        }
        System.out.println();

    }

    @Override
    public boolean isFinished() {
        return  string >= FINISH;
    }

    @Override
    public String exclaimVictory() {
        return "What's up doc.\n\n";
    }

    @Override
    public String toString() {
        return  getAscii();

    }

    @Override
    public void reset() {
        string = 0;
    }

    public String getAscii() {
        return ascii;
    }

    public void setAscii(String ascii) {
        this.ascii = ascii;
    }

}
