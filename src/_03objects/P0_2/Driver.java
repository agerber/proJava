package _03objects.P0_2;

/**
 Name your packages according to its Programming Exercise identifier, except replace the period with underscore.
 For example, if the Programming Exercise identifier is P1.15, then name your package P1_15
 */
public class Driver {

    public static void main(String[] args) {

        //Example: Create a program that pits two boxers against one another in an ultimate match to KO.
        //Each boxer has 100 hit points. When a boxer connects his punch, he extracts hit-points from his opponent
        //Each boxer has the following properties; accuracy, strength, hit-points, name, exclamation.

        //pseudocode
        //Create two boxers
        //While both still standing
            //pucher swings at punchee
            //swap puncher and punchee
        //victor exclaims victory


        Boxer boxApollo = new Boxer("Apollo", 4, 0.75);
        Boxer boxRocky = new Boxer("Rocky", 3, 0.85);

        Boxer boxPuncher = boxRocky; //Stalone draws first blood (no pun intended)
        Boxer boxPunchee = boxApollo;
        Boxer boxTemp;  //used for swapping

        int nC = 1;
        while(true){

            //swing
            boxPuncher.swing(boxPunchee);
            System.out.println(nC++ + " " + boxPuncher.getName() + " swings. " + boxPunchee.getName() + healthMeter(boxPunchee));

            if (boxPunchee.isKO()){
                //victor exclaims victory
                System.out.println(boxPuncher.meWin());
                break;
            }
            //swap
            boxTemp = boxPuncher;
            boxPuncher = boxPunchee;
            boxPunchee = boxTemp;

        }

    }

    private static String healthMeter(Boxer boxBoxer){

        String strR = " > ";
        for (int nC = 0; nC <boxBoxer.getHitPoints() ; nC++) {
              strR += "#";
        }
        return strR;
    }



}
