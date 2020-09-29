/*

import java.util.Scanner;

public class test5 {
    public static void main(String[] args) {
        int[] arr = {0, 1, 2, 3};
        int[] arr1 = getNextRightfulGuess(arr);
        for (int i = 0; i < arr.length; i++)
            System.out.println(arr1[i]);

    }


    public static int[] incrementOdometer1(int[] odometer) {
        int BASE = 10;
        int[] increasedOdometer = new int[odometer.length];
        boolean isChanged = false;
        for (int i = odometer.length - 1; i >= 0; i = i - 1) {
            if (!isChanged) {
                if (odometer[i] + 1 <= BASE - 1) {
                    increasedOdometer[i] = odometer[i] + 1;
                    isChanged = true;
                } else
                    increasedOdometer[i] = 0;
            } else {
                increasedOdometer[i] = odometer[i];
            }
        }
        return increasedOdometer;
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


    public static int[] getNextRightfulGuess(int[] guessArray) {
        int N = 4;
        int BASE = 10;
        int[] newRightfulGuess = guessArray;
        if (guessArray != null && guessArray.length != 0 && guessArray.length == N && isRightfulGuess1(guessArray)) {
            boolean isBiggest = true;
            int biggestNum = BASE - 1;
            for (int i = 0; i < N && isBiggest; i++) {
                if (guessArray[i] != biggestNum)
                    isBiggest = false;
                biggestNum = biggestNum - 1;
            }
            if (isBiggest) {
                for (int j = 0; j < N; j++) {
                    newRightfulGuess[j] = j;
                }
            } else {
                boolean isLowestNextGuess = false;
                while (!isLowestNextGuess) {
                    newRightfulGuess = incrementOdometer1(newRightfulGuess);
                    if (isRightfulGuess1(newRightfulGuess))
                        isLowestNextGuess = true;
                }
            }
        }
        return newRightfulGuess;
    }
}*/
