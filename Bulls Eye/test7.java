/*


import java.util.Scanner;

public class test7 {
    public static void main(String[] args) {
        int[] arr = {2,2};
        int[] arr1 = {0,1,2,3};
        int[]arr2 = {0,4,5,6};
        int[][][] history = new int[3][2][];
        history[0][0] = arr1;
        history[0][1] = arr;
        boolean results = settleGuessInHistory(1,arr2,history);
        System.out.println(results);


    }


  */
/*  public static boolean isRightfulGuess1(int[] guessArray) {
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
    }*//*


  //  public static boolean settleGuessInHistory(int round, int[] currentGuess, int[][][] gameHistory) {

//}







































*/
/*   boolean isRightNextGuess = true;
        int[] nextGuess = new int[currentGuess.length];
        for (int x=0;x<nextGuess.length;x++)
            nextGuess[x] = x;
        int hitFitSum;
        if (round == 0) {
            for (int x =0;x<currentGuess.length && isRightNextGuess;x++)
                if (currentGuess[x] != nextGuess[x])
                    isRightNextGuess = false;
        }
        else if(round == 1) {
            hitFitSum = gameHistory[0][1][0] + gameHistory[0][1][1];
            int i,hitValue = 0,tempValue=0,lastValue = nextGuess[nextGuess.length-1]+1;
            if (gameHistory[0][1][0] > 0)
                hitValue = gameHistory[0][1][0];
            if (gameHistory[0][1][1] > 0){
                for (int j =hitValue; j<currentGuess.length;j++){
                    if (j == hitValue)
                        tempValue = nextGuess[j];
                    else
                        nextGuess[j-1] = nextGuess[j];
                    if (j == nextGuess.length-1)
                        nextGuess[j] = tempValue;
                }
            }
            if (hitFitSum < currentGuess.length)
                for (int x = hitFitSum;x<nextGuess.length; x++) {
                    nextGuess[x] = lastValue;
                    lastValue++;
                }
            for (int x =0; x<currentGuess.length && isRightNextGuess;x++)
                if (currentGuess[x] != nextGuess[x])
                    isRightNextGuess = false;
        }else{








        }

        return isRightNextGuess;
    }
*/
