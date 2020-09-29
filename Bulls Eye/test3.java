/*
import java.util.Scanner;

public class test3 {
    public static void main(String[] args) {
        int[] arr={0,2,3,9};
        //int[] arr1 = new int[arr.length];
        int BASE = 10;
        System.out.println(isRightfulGuess1(arr,BASE));
        }

    public static boolean isRightfulGuess1(int[] guessArray,int x) {
        int N =4;
        boolean isRightFull = false;
        if(guessArray != null && guessArray.length != 0 && guessArray.length<= N){
            isRightFull = true;
            for (int i = 0 ; i < guessArray.length && isRightFull; i++){
                if (guessArray[i] > x -1)
                    isRightFull = false;
                else{
                    for (int j = i+1; j < guessArray.length && isRightFull; j++){
                        if (guessArray[i] == guessArray[j])
                            isRightFull = false;
                    }
                }
            }
        }

        return isRightFull;
    }





}*/
