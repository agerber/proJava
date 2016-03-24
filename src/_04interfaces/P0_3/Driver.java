package _04interfaces.P0_3;

/**
 Name your packages according to its Programming Exercise identifier, except replace the period with underscore.
 For example, if the Programming Exercise identifier is P1.15, then name your package P1_15
 */
public class Driver {


    public static void main(String[] args) {

        //pseudocode
        //Create a Raceable interface and two Raceable objects that implement the Raceable interface
        //race them 20 times.
            //while both still racing
                //stride and print out a histogram of progress
                //if either one crosses the finish-line
                    //the victor prints out some ascii art and exclaims victory
                    //break
            //reset racers to zero




        Raceable racOne = new Rabbit();
        Raceable racTwo = new Godzilla();

        for (int nC = 0; nC < 20 ; nC++) {
            System.out.println("New Race ====================================================");
            while (true){
               racOne.stride();
               if(racOne.isFinished()){
                   System.out.println(racOne);
                   System.out.println(racOne.exclaimVictory());
                   break;
               }
               racTwo.stride();
               if(racTwo.isFinished()){
                   System.out.println(racTwo);
                   System.out.println(racTwo.exclaimVictory());
                   break;
               }

            }
            racOne.reset();
            racTwo.reset();
        }

    }

}
