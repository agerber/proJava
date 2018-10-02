package _01control;

import java.util.Date;

public class P1_13 {


    public static void main(String[] args) {


       String[] wordsOfWisdom = "Jedi you are impatient".split(" ");
        for (int nC = wordsOfWisdom.length - 1; nC >= 0; nC--) {
            String word = wordsOfWisdom[nC];
            System.out.print(word);
            System.out.print(" ");
        }




    }


}
