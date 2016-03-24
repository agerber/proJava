package _02arrays;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 Name your source files according to its Programming Exercise identifier, except replace the period with underscore.
 For example, if the Programming Exercise identifier is P1.15, then name your java source file P1_15
 */
public class P0_1 {

    public static void main(String[] args) {

        //Example: Read each line of a textfile and print it to the console; prepended with a line number

        //pseudocode
        //Prompt user for absolute path to file
        //Open file for reading
        //declare integer counter and initialize to 1
        //while file has a next line
            //print the line number to console, then increment counter
            //print the line content to console + newline

        Scanner scan = new Scanner(System.in);
        //C:\dev\java\pros\proArrays\instructions.txt as test input
        System.out.print("Add line numbers. What is the absolute path to your file?:");
        String strPath = scan.next();
        File fileInput = new File(strPath);
        try {
            scan = new Scanner(fileInput);
        } catch (FileNotFoundException e) {
            System.out.println("There's been an error: " + e.getMessage());
            return;
        }

        int nC = 1;
        while(scan.hasNextLine()){
            System.out.print(toLineNumberString(nC++));
            System.out.println(scan.nextLine());
        }
    }

    private static String toLineNumberString(int nLine){
        if (nLine < 1){
            return "00:  ";
        } else if (nLine < 10){
            return "0" + nLine + ":  ";
        } else {
            return nLine + ":  ";
        }
    }



}
