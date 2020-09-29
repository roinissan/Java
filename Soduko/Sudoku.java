// Author: Roi Nissan 

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Sudoku {

	public static void main(String[] args) {

		int[][] board = readBoardFromFile("S1.txt");


	}

	// **************   Sudoku - Read Board From Input File   **************
	public static int[][] readBoardFromFile(String fileToRead){
		int[][] board = new int[9][9];
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToRead)); // change S1.txt to any file you like (S2.txt, ...)
			int row = 0;
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				for(int column = 0; column < line.length(); column++){
					char c = line.charAt(column);
					if(c == 'X')
						board[row][column] = 0;
					else board[row][column] = c - '0';
				}
				row++;
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return board;
	}



	// **************   Sudoku - Part1 (iterative)   **************
	/**
	* This function do the "elimniation process" - checks which values could fit to a cell,
	* if only 1 value is possible, the board will update the value to the cell, otherwise,
	* the values will be entered to the domainElimination array
	* */
	/*
	Each empty cell is checked which values of  its row,column and 3x3 board is possible to put
	 */
	public static int[][][] eliminateDomains(int[][] board) {
		int[] colArr = new int[9];
		int[] possibleNums = new int[1];
		int [][] subBoardArr;
		int[][][] finalDomainArr = new int[9][9][];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j]==0) {
					colArrCreator(board, j, colArr);
					subBoardArr = subArrDividor(board, whichSubBoardArr(i, j));
					possibleNums = eliminationCheck(board[i], colArr, subBoardArr);
					if (possibleNums.length == 1) {
						board[i][j] = possibleNums[0];
						finalDomainArr[i][j] = possibleNums;
						i = 0;
						j = 0;
					} else if (possibleNums.length > 1)
						finalDomainArr[i][j] = possibleNums;
					else
						finalDomainArr[i][j] = new int[0];
					possibleNums = new int[1];
				}
				else{
					possibleNums[0] = board[i][j];
					finalDomainArr[i][j] = possibleNums;
					possibleNums = new int[1];
				}
			}
		}
	return finalDomainArr;
	}

	/**
	 * This function prints the sudoku board and its domainElimination array
	 */
	public static void printBoard(int[][][] domains, int[][] board){
		for (int i =0;i<board.length;i++) {
			for (int j = 0; j < board[i].length; j++) {
				if ((i == 3 && j==0) || (i == 6 && j == 0))
					System.out.println("---+---+---");
				if ((j) % 3 == 0 && j != 0 )
					System.out.print("|");
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
		for (int i = 0; i < domains.length; i++) {
			for (int j = 0; j < domains[i].length; j++) {
				System.out.print(i + "," + j + " = " );
				for (int k = 0; k < domains[i][j].length; k++) {
					System.out.print (domains[i][j][k] + ",");
				}
				System.out.println();
			}
		}
	}


	// **************   Sudoku - Part2 (recursive)   **************

	/**
	 * This function receives a board of sudoku and makes the domain elimination proceess.
	 * after that,checks if the board is completed or if it contains less than 17 fixed values.
	 * if its neither of both, summons the "solve sudoku" to try solve the sudoku
	 * @return true or false.
	 */

	public static boolean solveSudoku(int[][] board) {
		boolean isComplete = true;
		int numOfEmptyCells = 0;
		int [] firstCellNotFilled={};
		int [][][] domaintEli = eliminateDomains(board);
		for (int i = 0; i < domaintEli.length; i++)
			for (int j = 0; j < domaintEli[0].length; j++) {
				if (domaintEli[i][j].length != 1){
					isComplete = false;
					if (numOfEmptyCells == 0)
						firstCellNotFilled = domaintEli[i][j];
					numOfEmptyCells ++;
				}
			}
		if (isComplete)
			return true;
		else if(numOfEmptyCells > 64)
			return false;
		boolean answer = solveSudoku(board,domaintEli,0,0);
		return answer;

	}

	/**
	 * checks each board cell, it its empty - try to put the possible values of the domain eliminaion array,
	 * if it fails, the function doing backtraking to try a new value.
	 * if the process Succeeded update the board.
	 * @return true or false
	 */

	public static boolean solveSudoku(int[][] board,int [][][]domainArr,int row,int col) {
		int[] nextCell;
		//boolean isEmpty = false;
		//int rowIndex, colIndex;
		if (row == 9 && col == 9)
			return true;
		nextCell = nextCell(row, col);
		if (board[row][col] != 0) {
			if (solveSudoku(board, domainArr, nextCell[0], nextCell[1]))
				return true;
		}else{
			int[] possibleValues = domainArr[row][col];
			int[] colArr = new int[9];
			colArrCreator(board, col, colArr);
			int[][] subBoardArr = subArrDividor(board, whichSubBoardArr(row, col));
			for (int k = 0; k < possibleValues.length; k++) {
				if (!isContainNum(board[row], colArr, subBoardArr, possibleValues[k])) {
					int[][] copyArr = new int[9][9];
					for (int p = 0; p < board.length; p++)
						for (int x = 0; x < board[0].length; x++)
							copyArr[p][x] = board[p][x];
					copyArr[row][col] = possibleValues[k];
					if (solveSudoku(copyArr, domainArr, nextCell[0], nextCell[1])) {
						for (int p = 0; p < board.length; p++)
							for (int x = 0; x < board[0].length; x++)
								board[p][x] = copyArr[p][x];
						return true;
					} else
						board[row][col] = 0;
				}
			}

		}
		return false; //start of backtracking
	}

	/*
		function to find create an column array to specific index of the sudoku board.
 	*/
	public static void colArrCreator(int [][] boardArr, int jIndex,int [] colArr){
			for (int k = 0; k < 9; k++)
				colArr[k] = boardArr[k][jIndex];
	}

	/*
	this function receives 2 indexes (row and column) and returns the number of its corresponding sub 3x3 array,
	the arrays are numbered from the left upper corner.
	 */
	public static int whichSubBoardArr(int iIndex, int jIndex){
		int rightIndex=0;
		if ((iIndex>= 0 && iIndex < 3) && (jIndex >= 0 && jIndex < 3))
			rightIndex = 1;
		else if ((iIndex>= 0 && iIndex < 3) && (jIndex >= 3 && jIndex < 6))
			rightIndex = 2;
		else if ((iIndex>= 0 && iIndex < 3) && (jIndex >= 6 && jIndex < 9))
			rightIndex = 3;
		else if ((iIndex>= 3 && iIndex < 6) && (jIndex >= 0 && jIndex < 3))
			rightIndex = 4;
		else if ((iIndex>= 3 && iIndex < 6) && (jIndex >= 3 && jIndex < 6))
			rightIndex = 5;
		else if ((iIndex>= 3 && iIndex < 6) && (jIndex >= 6 && jIndex < 9))
			rightIndex = 6;
		else if ((iIndex>= 6 && iIndex < 9) && (jIndex >= 0 && jIndex < 3))
			rightIndex = 7;
		else if ((iIndex>= 6 && iIndex < 9) && (jIndex >= 3 && jIndex < 6))
			rightIndex = 8;
		else if ((iIndex>= 6 && iIndex < 9) && (jIndex >= 6 && jIndex < 9))
			rightIndex = 9;

		return rightIndex;
	}

	/*
	This functions receives the sub array index and returns the right sub array.
	(continuation to the previous function)
	 */

	public static int [][] subArrDividor(int [][]boardArr,int subBoardIndex){
		int [][] subBoardArr = new int[3][3];
		if (subBoardIndex == 1)
			for (int i = 0; i < 3; i++)
				for (int j = 0; j <3; j++)
					subBoardArr[i][j] = boardArr[i][j];
		else if(subBoardIndex ==2)
			for (int i = 0; i < 3; i++)
				for (int j = 3; j < 6 ; j++)
					subBoardArr[i][j-3] = boardArr[i][j];
		else if(subBoardIndex ==3)
			for (int i = 0; i < 3; i++)
				for (int j = 6; j < 9 ; j++)
					subBoardArr[i][j-6] = boardArr[i][j];
		else if(subBoardIndex ==4)
			for (int i = 3; i < 6; i++)
				for (int j = 0; j < 3 ; j++)
					subBoardArr[i-3][j] = boardArr[i][j];
		else if(subBoardIndex ==5)
			for (int i = 3; i < 6; i++)
				for (int j = 3; j < 6 ; j++)
					subBoardArr[i-3][j-3] = boardArr[i][j];
		else if(subBoardIndex ==6)
			for (int i = 3; i < 6; i++)
				for (int j = 6; j < 9 ; j++)
					subBoardArr[i-3][j-6] = boardArr[i][j];
		else if(subBoardIndex ==7)
			for (int i = 6; i < 9; i++)
				for (int j = 0; j < 3 ; j++)
					subBoardArr[i-6][j] = boardArr[i][j];
		else if(subBoardIndex ==8)
			for (int i = 6; i < 9; i++)
				for (int j = 3; j < 6 ; j++)
					subBoardArr[i-6][j-3] = boardArr[i][j];
		else if(subBoardIndex ==9)
			for (int i = 6; i < 9; i++)
				for (int j = 6; j < 9 ; j++)
					subBoardArr[i-6][j-6] = boardArr[i][j];
		return subBoardArr;
	}

	/*
		This function receives 3 arguments - row,column and subboard.
		it checks which numbers from Base 10 (0 not included), non of the arguments contains.
		returns an array of those numbers.
	 */

	public static int [] eliminationCheck(int [] row,int [] col,int [][] subBoard){
		boolean isIntegerIn;
		int [] firstSubBoardRow = subBoard[0], secondSubBoardRow = subBoard[1],thirdSubBoardRow = subBoard[2];
		int [] possibleInts = {},checkArray = new int [9];
		int posibbleNumCounter = 0;
		for (int i = 1; i <= 9; i++) {
			isIntegerIn = false;
			for (int j = 0; j < row.length && !isIntegerIn; j++) {
				if (j < 3)
					if ( i==firstSubBoardRow[j] || i == secondSubBoardRow[j] || i == thirdSubBoardRow [j])
						isIntegerIn = true;
				if(!isIntegerIn)
					if (i == row[j] || i == col[j])
						isIntegerIn = true;
			}
			if(!isIntegerIn){
				checkArray[posibbleNumCounter] = i;
				posibbleNumCounter++;
			}
		}
		if(posibbleNumCounter > 0){
			possibleInts = new int[posibbleNumCounter];
			for (int i = 0; i < posibbleNumCounter; i++) {
				possibleInts[i] = checkArray[i];
			}
		}
		return possibleInts;
	}


	/*
		This function receives  4 arguments - row,column,subboard and a number.
		it checks if the number is found in any of the other arguments.
		returns true of false.
	 */
	static public boolean isContainNum(int [] row, int [] col, int [][] subArr, int num){
		boolean isIntegerIn=false;
		int [] firstSubBoardRow = subArr[0], secondSubBoardRow = subArr[1],thirdSubBoardRow = subArr[2];
			for (int j = 0; j < row.length && !isIntegerIn; j++) {
				if (j < 3)
					if (num == firstSubBoardRow[j] || num == secondSubBoardRow[j] || num == thirdSubBoardRow[j])
						isIntegerIn = true;
				if (!isIntegerIn)
					if (num == row[j] || num == col[j])
						isIntegerIn = true;
			}
		return isIntegerIn;
	}

	/*
		This function checks which cell should come after ,based of its arguments(row and col).
		return an array with its indexs.
	 */
	static public int[] nextCell(int row, int col){
		int []nextCellArr = new int[2];
		if (row == 8 && col == 8){
			nextCellArr[0] = 9;
			nextCellArr[1] = 9;
		}
		else if(col == 8){
			nextCellArr[0] = row +1;
			nextCellArr[1] = 0;
		}
		else if(row == 8) {
			nextCellArr[0] = row;
			nextCellArr[1] = col + 1;
		}
		else{
			nextCellArr[0] = row;
			nextCellArr[1] = col +1;
		}

		return nextCellArr;
	}

}
