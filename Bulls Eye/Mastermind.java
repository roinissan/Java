//Author: Roi Nisan 

public class Mastermind
{
	public static int N = 4; 					// Number of digits in a tuple
	public static int BASE = 10;				// Base of the digits
	public static int MAX_ROUND_NUMBER = 6;		// Number of rounds in a game

	public static void main(String[] args) {
		Frame.play();
	
	/*	int []arr = randomizeSequence();
		for (int i =0 ; i <N;i++)
			System.out.println(arr[i]);
*/
		// TO TEST YOUR CODE MARK THE LINE  "Frame.play();"  AS A REMRAK AND UNMARK THE REQUESTED TEST:


		/* //+++++++++++++++++++++++++ test for areArraysEqual +++++++++++++++++++++++++++++++

		int[]arr1 = {1,2,3,9};
		int[]arr2 = {1,2,3,9};
		int[]arr3 = {1,2,4,0};
		int[]arr4 = {1,2,3};
		System.out.println(areArraysEqual(arr1,arr2));
		System.out.println(areArraysEqual(arr1, arr3));
		System.out.println(areArraysEqual(arr1, arr4));

		// expected output: true
		//				    false
		// 			 	    false

		*/



	/*	  //+++++++++++++++++++++++++ test for incrementOdometer ++++++++++++++++++++++++++++++

		int[]arr5 = {0,0,9,9};
		int[]arr6 = null;
		arr6 = incrementOdometer(arr5);
		for (int i = 0; i < arr6.length; i++) {
			System.out.print(arr6[i]+ " ");
		}
		System.out.println();

		// expected output: 0 1 0 0

		*/



		/*  +++++++++++++++++++++++++ test for isRightfulGuess +++++++++++++++++++++++++++

		int[]arr7 = {0,2,3,9};
		int[]arr8 = {2,1,3,2};
		
		System.out.println(isRightfulGuess(arr7));
		System.out.println(isRightfulGuess(arr8));

		// expected output: true
		//			 	    false

		*/



	/*	 +++++++++++++++++++++++++ test for getNextRightfulGuess +++++++++++++++++++++++++

		int[] arr2 = {0,0,0,0};
		int[] arr1 = getNextRightfulGuess(arr2);
		for (int i = 0; i < arr1.length; i++) {
			System.out.print(arr1[i]+ " ");
		}
		System.out.println();

		// expected output: 1 2 4 0
		*/




		 /* +++++++++++++++++++++++++ test for judgeBetweenGuesses +++++++++++++++++++++++++++++++++++++

		int[] guess1 = {2,1,3,4};
		int[] guess2 = {4,1,3,2};
		int[] hitFitAnswer = judgeBetweenGuesses(guess1, guess2);
		System.out.print(hitFitAnswer[0] + " " + hitFitAnswer[1]);
		System.out.println();

		// expected output: 2 2

		*/


		 /* +++++++++++++++++++++++++ test for printGame and play +++++++++++++++++++++++++

		int[] arr1 = {3,8,9,5};
		printGame(arr1, play(arr1));

		//expected output:  the secret is 1 2 3 4
		//			  		the guess 0 1 2 3  gives (h/f): 0 3
		//			  		the guess 1 0 3 4  gives (h/f): 3 0
		//			 		 the guess 1 0 3 5  gives (h/f): 2 0
		//			 		 the guess 1 2 3 4  gives (h/f): 4 0
		//			 		 you guessed the secret within 4  rounds

		*/

	} // end of main




	/**
	  * This function receives 2 arrays and returns "true" if both have the same vales,
	  * in the same order and have the length,otherwise will return "false".
	 **/
	public static boolean areArraysEqual(int[] firstArr, int[] secArr) {
		boolean isEqual = true;
		if (firstArr == null || secArr == null || firstArr.length!= secArr.length)
			isEqual = false;
		else{
			int element = 0; // to loop through the arrays.
			//Checks is each element of the arrays are equals.
			while (isEqual && element < firstArr.length){
				if (firstArr[element] != secArr[element] )
					isEqual = false;
				element ++;
			}
		}
		return isEqual;
	}

	/**
	* This function returns array in length "N" so that each element in the array has different value.
	 **/
	public static int[] randomizeSequence() {
		int [] randomArray = {}; // for case when BASE<1
		if (N > 0 && BASE >= N ){
			int baseChecker = BASE -1,numOfRandomDigits = 10;
			while(baseChecker/10 > 0){
				numOfRandomDigits = numOfRandomDigits * 10;
				baseChecker = baseChecker/10;
			}
			randomArray = new int[N];
			Boolean isNumInArray = false;
			int randomNum, numCounter = 0; // counter to check if the array recieved N inputs.
			while ( numCounter < N) {
				isNumInArray = false;
				randomNum = (int) (Math.random() * numOfRandomDigits);
				if (randomNum < BASE) {
					//first case when the array is empty.
					if (numCounter == 0) {
						randomArray[0] = randomNum;
						numCounter++;
					} else {
						// loop to check if the random number is already in the array.
						for (int i = 0; i < numCounter && !isNumInArray; i++) {
							if (randomNum == randomArray[i])
								isNumInArray = true;
						}
						if (!isNumInArray) {
							randomArray[numCounter] = randomNum;
							numCounter++;
						}
					}
				}
			}
		}
		return randomArray;
	}

	/**
	 * This function receives array of number which represent a number in base BASE,
	 * and returns a new array with which represent the following number.
	 *
	 **/
	public static int[] incrementOdometer(int[] odometer) {
		boolean isValid = true;
		int[] increasedOdometer = {};
		if (odometer == null || BASE < 1 || !isValidArray(odometer))
			isValid = false;

		if (isValid) {
			increasedOdometer = new int[odometer.length];
			boolean isChanged = false; // to check if any value is already incremented

			/*a loop which iterates each element of the array starting from the end and checks if it can be increased by 1.
			if it does the value is updated if not the value will be 0 .after that the rest of the original array is copied to the new array
			 */
			for (int i = odometer.length - 1; i >= 0; i = i - 1) {
				if (!isChanged) {
					if (odometer[i] + 1 <= BASE - 1) {
						increasedOdometer[i] = odometer[i] + 1;
						isChanged = true;
					} else
						increasedOdometer[i] = 0;
				}else{
					increasedOdometer[i] = odometer[i];
				}
			}
		}
		return increasedOdometer;
	}

	/**
	 * This function receives array of numbers and return "true"
	 * if the array is valid guess ( all the numbers are different and in base BASE)
	 * otherwise will return false
	 **/
	public static boolean isRightfulGuess(int[] guessArray) {
		boolean isRightFull = false;
		// first if condition to check the basic criteria
		if(guessArray != null && N > 0 && BASE >= N  && guessArray.length == N ){
			isRightFull = true;
			// 2 loops which iterates starting from the first element to check  it has equals values or if it is bigger than the BASE.
			for (int i = 0 ; i < guessArray.length-1 && isRightFull; i++){
				if (guessArray[i] > BASE -1)
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

	/**
	 * This function receives a guess in array and returns the following guess
	 * to that guess.
	 **/
	public static int[] getNextRightfulGuess(int[] guessArray) {
		int [] newRightfulGuess = {};
		if (guessArray != null && N > 0 && BASE >= N  && guessArray.length == N && isValidArray(guessArray)){
			newRightfulGuess = guessArray;
			//first check if the array is the biggest guess if it does return the lowest possible guess in BASE.
			boolean isBiggest = true;
			int biggestNum = BASE -1;
			for (int i = 0 ; i < N && isBiggest ; i++){
				if(guessArray[i] != biggestNum)
					isBiggest = false;
				biggestNum = biggestNum -1;
			}if (isBiggest){
				for (int j = 0; j < N; j++){
					newRightfulGuess[j] = j;
				}
			}
			/*if the guess is not the biggest, finding the lowest guess after the current guess, by increasing the digis starting from the last element
			with  incrementOdometer function, and checking if the result is valid with isRightfulGuess function.
			*/
			else{
				boolean isLowestNextGuess = false;
				while(!isLowestNextGuess){
					newRightfulGuess = incrementOdometer(newRightfulGuess);
					if (isRightfulGuess(newRightfulGuess))
						isLowestNextGuess = true;
				}
			}
		}
		return newRightfulGuess;
	}


	/**
	 * This function receive to guesses in 2 arrays, and returns how many elements int the same index,
	 * are equal as "hit" and how many elements are equal in different indexes as "fit"
	 * the return will be a array in length 2(first element is hit and second is fit)
	 **/
	public static int[] judgeBetweenGuesses(int[] guess1, int[] guess2) {
		int[] hitFitArray = {};
		if (isRightfulGuess(guess1) && isRightfulGuess(guess2)){
			 hitFitArray = new int[2];
			int hitCounter = 0, fitCounter = 0;
			/*first checks if the value in the same index of the two guesses is equal, if it is- increase hitCount,
			otherwise checks if the value in index i of the guess1 has equal value in any index at guess2.
			 */
			for(int i = 0; i < guess1.length; i++){
				if (guess1[i] == guess2 [i])
					hitCounter++;
				else
					for(int j = 0; j <guess2.length; j++)
						if (guess1[i] == guess2[j])
							fitCounter++;
			}
			hitFitArray[0] = hitCounter;
			hitFitArray[1] = fitCounter;
		}
		return hitFitArray;
	}

	/**
	 * This function receives a guess and checks if the guess is settles with all the
	 * previous guesses .(Settles means that the current guess could replace  each of previous
	 * guesses and will yield the same result).
	 **/
	public static boolean settleGuessInHistory(int round, int[] currentGuess, int[][][] gameHistory) {
		//first condition to check if the values are valid
		boolean isNotNull = true;
		boolean isRightNextGuess = false;
		if (currentGuess == null)
			isNotNull = false;
		else{
			for (int i =0; i<round ; i++){
				if (gameHistory[i][0]== null || gameHistory[i][1]==null)
				isNotNull = false;
			}
		}
		if (isNotNull){
			/*each round in history is compared to the current guess to see if its settle with the hit-fit result, its done by
			comparing the hit-fit result between the history round and the current guess to the hit-fit result of the history round.
			*/
			isRightNextGuess = true;
			for (int iRound = 0; iRound < round && isRightNextGuess; iRound++){
				int roundHit = gameHistory[iRound][1][0];
				int roundFit = gameHistory[iRound][1][1];
				int [] hitFitOfHistoryCurrentGuess = judgeBetweenGuesses(currentGuess,gameHistory[iRound][0]);
				if (areArraysEqual(currentGuess,gameHistory[iRound][0]))
					isRightNextGuess = false;
				else if(roundHit != hitFitOfHistoryCurrentGuess[0] || roundFit != hitFitOfHistoryCurrentGuess[1])
					isRightNextGuess = false;

				}
		}
		return isRightNextGuess;
	}

	/**
	 * This function receives the results of the current round and update them in the game history.
	 **/
	public static void update(int[][][] gameHistory, int round, int[] newGuess, int[] score) {
		// adds a newGuess and answer (hits/fits) to the gameHistory at
		// "line" round. We assume that round < MAX_ROUND_NUMBER.
		if ( round >=0 && isRightfulGuess(newGuess) && score != null){
			gameHistory[round][0] = newGuess;
			gameHistory[round][1] = score;
		}
	}  

	/**
	 * This function will execute the strategy of the game, it will return the history of the game
	 * based on the received secret
	 **/
	public static int[][][] play(int[] secret) {

		boolean found = false;
		int[][][] gameHistory = new int[MAX_ROUND_NUMBER][2][];
		int[] currentGuessArray = new int[N];

		for (int i=0; i<N; i=i+1) {
			currentGuessArray[i] = 0;
		}

		int round = 0;
		while (round < MAX_ROUND_NUMBER && !found) {
			boolean isValid = false;
			if (round == 0) {
				for (int i = 0; i < N; i++)
					currentGuessArray[i] = i;
				update(gameHistory, round, currentGuessArray, judgeBetweenGuesses(currentGuessArray, secret));
			} else {
				while (!isValid) {
					currentGuessArray = getNextRightfulGuess(currentGuessArray);
					if (settleGuessInHistory(round, currentGuessArray, gameHistory))
						isValid = true;
				}
				update(gameHistory, round, currentGuessArray, judgeBetweenGuesses(currentGuessArray, secret));
			}
			if (areArraysEqual(currentGuessArray, secret))
				found = true;
			round++;
		}
		return gameHistory;
	}

	/**
	 * This function will run the game and print its results.
	 **/
	public static void printGame(int[] secret, int[][][] gameHistory) {
		int roundsCounter = 0;
		System.out.print("The secret is ");
		for (int i =0; i<N ; i++){
			if (i == N -1)
				System.out.println(secret[i]+ " ");
			else
				System.out.print(secret[i]+ " ");
		}
		for (int i = 0; i<MAX_ROUND_NUMBER;i++){
			if (gameHistory[i][0] != null) {
				System.out.print("The guess ");
				for (int j = 0; j < N; j++)
					System.out.print(gameHistory[i][0][j] + " ");
				System.out.println("gives (h/f): " + gameHistory[i][1][0] + " " + gameHistory[i][1][1] + " ");
				roundsCounter++;
			}
		}
		if(areArraysEqual(secret,gameHistory[roundsCounter-1][0]))
			System.out.print("You guessed the secret within "+ roundsCounter + " rounds");
		else
			System.out.print("You failed guessing the secret within "+"MAX_ROUND_NUMBER="+MAX_ROUND_NUMBER + " rounds");
	}

	/**
	 * function to validate that arr1 contains only number in base BASE
	 */
	public static boolean isValidArray(int[] arr1){
		boolean isValid = true;
		for (int i = 0; i < arr1.length && isValid; i++)
			if (arr1[i] >= BASE || arr1[i] < 0)
				isValid = false;
		return isValid;
	}

}// end of class Mastermind
