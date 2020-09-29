// Author: Roi Nissan

public class Warmup {

	public static void main(String[] args) {

	}
	
	// **************   Task1   **************

	/**
	 * This function if a given digit is contained in a specific number.
	 * @return true or false
	 */

	public static boolean doesDigitAppearInNumber(int number, int digit) {
		boolean isDigitIn;
		if (number == 0){
			isDigitIn = false;
			return isDigitIn;
		}
		else if (number%10 == digit) {
			isDigitIn = true;
			return isDigitIn;
		}
		isDigitIn = doesDigitAppearInNumber(number/10,digit);
		return isDigitIn;
	}

	// ************** Task2 **************

	/**
	 * This function recives a number and return how many even digits it contains
	 */

	public static int countNumberOfEvenDigits(int number) {
		int countHowManyEven=0;
		if (number == 0){
			countHowManyEven = 0;
			return countHowManyEven;
		}
		else if ((number%10)%2==0){
			countHowManyEven = 1;
			countHowManyEven = countHowManyEven + countNumberOfEvenDigits(number/10);
			return countHowManyEven;
		}else{
			countHowManyEven = countNumberOfEvenDigits(number/10);
		}
		return countHowManyEven;
	}

	// ************** Task3 **************

	/**
	 * This function receives a string and a char
	 * @returns how many times the char is in the string.
	 */

	public static int countTheAmountOfCharInString(String str, char c) {
		return countTheAmountOfCharInString(str,c,0);
	}
	public static int countTheAmountOfCharInString(String str, char c,int index) {
		int howManyEqualChars = 0;
		int isCharIn = str.substring(index).indexOf(c);
		if (index == str.length())
			return 0;
		else if (isCharIn != -1) {
			howManyEqualChars = 1;
			howManyEqualChars = howManyEqualChars + countTheAmountOfCharInString(str, c, index+ 1 + isCharIn);
			return howManyEqualChars;
		}
		return howManyEqualChars;
	}

	// ************** Task4 **************

	/**
	 * This function receives a string and checks if it contains only lowercase or uppercase chars
	 * it is done by checking each char is in the ascii range of lower or upper case.
	 * @return true or false.
	 */

	public static boolean checkIfAllLettersAreCapitalOrSmall(String str) {
		boolean isAllCharsSameCase;
		if(str.length()<=1)
			isAllCharsSameCase = true;
		else
			if ((int)str.charAt(0) >= 97)
				isAllCharsSameCase = checkIfAllLettersAreCapitalOrSmall(str,1,1);
			else
				isAllCharsSameCase = checkIfAllLettersAreCapitalOrSmall(str,1,0);

		return isAllCharsSameCase;
	}
	/*
		if Identifier equals "1" it means that the string starts with lower case
		if it equals "0" it means uppercase.
	 */
	public static boolean checkIfAllLettersAreCapitalOrSmall(String str,int index,int identifier){
		if(index == str.length())
			return true;
		else if (((int)str.charAt(index)< 97&& identifier == 1) ||((int)str.charAt(index) >90 && identifier ==0))
				return false;
		return checkIfAllLettersAreCapitalOrSmall(str,index +1,identifier);
	}

}
