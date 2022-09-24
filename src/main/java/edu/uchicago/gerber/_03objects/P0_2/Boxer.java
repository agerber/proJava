package edu.uchicago.gerber._03objects.P0_2;

import java.util.Random;

public class Boxer {


    private static final int MAX_HIT_POINTS = 100;
    private static final String[] EXCLAMS = {"I'm the greatest", "Float like a butterfly, sting like a bee", "Victory is mine", "Haahaahahaha.", "Adrian!"};
    //Each boxer has the following properties; accuracy, strength, hit-points, name, exclamation.
    private double accuracy;   //percentage
    private int strength;      //how strong is the fighter, as in much does it sting when he connects  1-5
    private int hitpoints;     //initialize to 100
    private String name;  //fighters name
    private Random random;


    public Boxer(String name, int strength, double accuracy) {
        this.name = name;
        this.strength = strength;
        this.accuracy = accuracy;
        hitpoints = MAX_HIT_POINTS;
        random = new Random();
    }


    public void swing(Boxer boxPunchee){

      if ((random.nextInt(10) + 1) * accuracy > 5){
          boxPunchee.setHitPoints(boxPunchee.getHitPoints() - strength);
      }

    }

    public boolean isKO(){
        return hitpoints <= 0;
    }

    public String meWin(){
        return name + " wins: " + EXCLAMS[random.nextInt(EXCLAMS.length)];
    }


    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHitPoints() {
        return hitpoints;
    }

    public void setHitPoints(int hitPoints) {
        hitpoints = hitPoints;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}


