package _01control;

import java.util.ArrayList;
import java.util.Scanner;

/**
 Name your source files according to its Programming Exercise identifier, except replace the period with underscore.
 For example, if the Programming Exercise identifier is P1.15, then name your java source file P1_15
 */
public class P0_0 {

    public static void main(String[] args) {

        //Example: Find the max value of an indefinite series of integers entered by the user in the command-line

        //pseudocode
        //while quit is not entered
            //ask user for integer value or type quit to exit
            //store integer value in ArrayList
        //assume the first entry in the ArrayList is the max value
        //for each value in ArrayList - starting with second entry
            //if value > max
                // set max to value
        //print the max value to the console


        Scanner scan = new Scanner(System.in);
        //use the wrapper class Integer. Use diamond notation from Java7 and omit the right-hand "Integer" if you want
        ArrayList<Integer> intValues = new ArrayList<Integer>();

        while(true){
            try {
                System.out.print("Type integer value or \"quit\" to exit:");
                //any value that is not an integer (including "quit") will throw an exception, which breaks out of the loop
                intValues.add(scan.nextInt());
            } catch ( Exception e) {
                break;
            }
        }
        if (intValues.size() == 0){
            System.out.println("Not enough data");
            return;
        }

        //let's assume the zero'th element is the max
        Integer intMax =  intValues.get(0);
        Integer intTest;
        //from 1 to the end of ArrayList
        for (int nC = 1; nC < intValues.size(); nC++) {
            intTest = intValues.get(nC);
            if(intTest > intMax){
                intMax = intTest;
            }
        }
        System.out.println("Max value is " + intMax);

    }
}
