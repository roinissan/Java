/*

import java.util.Scanner;

public class test6 {
    public static void main(String[] args) {
        int[] arr = {2,1,3,4};
        int[] arr1 = {4,1,3,2};
        int[]results = judgeBetweenGuesses(arr,arr1);
        System.out.println(results[0]);
        System.out.println(results[1]);

    }


    public static boolean isRightfulGuess1(int[] guessArray) {
        int N = 4;
        int BASE = 10;
        boolean isRightFull = false;
        if (guessArray != null && guessArray.length != 0 && guessArray.length <= N) {
            isRightFull = true;
            for (int i = 0; i < guessArray.length && isRightFull; i++) {
                if (guessArray[i] > BASE - 1)
                    isRightFull = false;
                else {
                    for (int j = i + 1; j < guessArray.length && isRightFull; j++) {
                        if (guessArray[i] == guessArray[j])
                            isRightFull = false;
                    }
                }
            }
        }

        return isRightFull;
    }

    public static int[] judgeBetweenGuesses(int[] guess1, int[] guess2) {
        int[] hitFirArray = new int[2];
        if (isRightfulGuess1(guess1) && isRightfulGuess1(guess2)){
            int hitCounter = 0, fitCounter = 0;
            for(int i = 0; i < guess1.length; i++){
                if (guess1[i] == guess2 [i])
                    hitCounter++;
                else
                    for(int j = 0; j <guess2.length; j++)
                        if (guess1[i] == guess2[j])
                            fitCounter++;

            }
            hitFirArray[0] = hitCounter;
            hitFirArray[1] = fitCounter;
        }
        return hitFirArray;
    }



}*/
