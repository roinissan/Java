/**
 * This class represent a board of domino game
 * @author roi nissan 
 */

public class Board {
	/**
	 * 3 fileds :
	 * numOfTiles - indicates the length of the board(max number of domino tiles which could be on the board).
	 * left/rightBoard - a board to each move - add to the left or right board.
	 */

	private int numOfTiles;
	private Tile [] leftBoard;
	private Tile [] rightBoard;

	/**
	 * Constructor with one parameter - number of tiles , which initiate the board size based on the parameter.
	 * @param numOfTiles
	 */

	public Board(int numOfTiles) {
		if (numOfTiles > 0 && numOfTiles < 29){
			this.numOfTiles = numOfTiles;
			this.leftBoard = new Tile [numOfTiles];
			this.rightBoard = new Tile [numOfTiles];
		}
		else if(numOfTiles <= 0){
			this.leftBoard = new Tile [1];
			this.rightBoard = new Tile [1];
		}
		else{
			this.leftBoard = new Tile [28];
			this.rightBoard = new Tile [28];
		}
	}

	/**
	 * A copy constructor - receives a board object and copy its fields.
	 * @param copy
	 */

	public Board(Board copy){
		if(copy != null){
			this.rightBoard = copy.rightBoard;
			this.leftBoard = copy.leftBoard;
			this.numOfTiles = copy.numOfTiles;
		}
	}

	/**
	 * Method to return the very last right value in the board.
	 * @return int value
	 */

	public int getRightValue() {
		return checkBoard(rightBoard,0);
	}

	/**
	 * Method to return the very last left value in the board.
	 * @return int value
	 */

	public int getLeftValue() {
		return checkBoard(leftBoard,1);
	}

	/**
	 * Method to return the current board in its current status
	 * @return array of tiles
	 */

	public Tile[] getBoard() {
		if (rightBoard == null || rightBoard[0] == null)
			return null;
		// left and right getter functions - returns a tile array without null objects.
		Tile [] newLeftBoard = leftBoardGetter();
		Tile [] newRightBoard = rightBoardGetter();
		Tile [] returnRightLefttBoard = new Tile[newRightBoard.length -1 + newLeftBoard.length];
		int lastIndex = 0, rightBoardIndex = 1;
		for (int i = newLeftBoard.length -1; i > -1; i--) {
			returnRightLefttBoard[lastIndex] = new Tile(newLeftBoard[i]);
			lastIndex++;
		}
		for (int i = lastIndex; i < newRightBoard.length-1 + newLeftBoard.length ; i++) {
			returnRightLefttBoard[i] = new Tile(newRightBoard[rightBoardIndex]);
			rightBoardIndex++;
		}
		return returnRightLefttBoard;
	}

	/**
	 * Method to add a tile to the right side of the board, return false if impossible ,true otherwise
	 * @param tile
	 * @return true or false
	 */

	public boolean addToRightEnd (Tile tile){
		if(tile == null)
			return false;
		Tile copy = new Tile(tile);
		boolean addOrNot = false;
		//case when the board is empty.
		if (this.rightBoard[0] == null){
			this.rightBoard[0] =copy;
			this.leftBoard[0] = copy;
			addOrNot = true;

		}else{
			// isPossible method to check if the tile could be assign to the right end of the board.
			if(isPossibleToAdd(copy,0)){
				addOrNot = true;
				if(this.getRightValue() == copy.getRightNumber())
					copy.flipTile();
				addToBoard(copy, 0);
			}
		}
		return addOrNot;
	}


	/**
	 * Method to add a tile to the left side of the board, return false if impossible ,true otherwise
	 * @param tile
	 * @return true or false
	 */

	public boolean addToLeftEnd (Tile tile){
		if(tile == null)
			return false;
		Tile copy = new Tile(tile);
		boolean addOrNot = false;
		//case when the board is empty.
		if (this.leftBoard[0] == null){
			this.rightBoard[0] = copy;
			this.leftBoard[0] = copy;
			addOrNot = true;
		}else{
			// isPossible method to check if the tile could be assign to the left end of the board.
			if(isPossibleToAdd(copy,1)){
				addOrNot = true;
				if(this.getLeftValue() == copy.getLeftNumber())
					copy.flipTile();
				addToBoard(copy, 1);
			}
		}
		return addOrNot;
	}

	/**
	 * toString method to create a special format to the board.
	 * @return string with all of the tiles on the board by their order.
	 */

	@Override
	public String toString() {
		String toSting = "";
		if(rightBoard !=null && rightBoard.length > 0 && rightBoard[0] != null){
			Tile [] board = this.getBoard();
			for (int i = 0; i < board.length; i++) {
				if (i == board.length-1)
					toSting = toSting + board[i].toString();
				else
					toSting = toSting + board[i].toString() + ",";
			}
		}
		return toSting;
	}


	/**
	 * equals method to check if a given board is the same as the current board
	 * @param boardToEqual of object Tile
	 * @return true or false.
	 */

	@Override
	public boolean equals(Object boardToEqual) {
		if(boardToEqual instanceof Board == false)
			return false;
		Board toEqual = (Board)boardToEqual;
		if(toEqual.getBoard() == null && this.getBoard() == null)
			return true;
		if (toEqual.getBoard() == null || this.getBoard() == null ||this.getBoard().length != toEqual.getBoard().length)
			return false;
		for (int i = 0; i < toEqual.getBoard().length; i++) {
			if(toEqual.getRightValue()!= getRightValue() || toEqual.getLeftValue()!= getLeftValue())
				return false;
		}
		return true;
	}

	/**
	 * A private method to return the last number from the last tile of array - used with getRight/LeftValue methods.
	 * @param leftOrRightBoard
	 * @param rightOrLeft - 0 represent right and 1 left.
	 * @return int number
	 */

	private int checkBoard(Tile [] leftOrRightBoard , int rightOrLeft ){
		if (leftOrRightBoard == null || leftOrRightBoard[0] == null || leftOrRightBoard.length == 0)
			return -1;
		int ans = -1;
		boolean isLast = false;
		for (int i = 1;i < numOfTiles && !isLast ; i++){
			//case to the last element of the array.
			if(i == numOfTiles-1 && leftOrRightBoard[i]!= null) {
				if ( rightOrLeft == 0)
					ans = leftOrRightBoard[i].getRightNumber();
				else
					ans = leftOrRightBoard[i].getLeftNumber();
			}
			else if (leftOrRightBoard[i] == null){
				if ( rightOrLeft == 0)
					ans = leftOrRightBoard[i-1].getRightNumber();
				else
					ans = leftOrRightBoard[i-1].getLeftNumber();
				isLast = true;
			}
		}
		return ans;
	}


	/**
	 * A private method to add a new tile to the board - used with addToleft/RightEnd methods
	 * @param tile
	 * @param rightOrLeft
	 */

	private void addToBoard(Tile tile ,int rightOrLeft) {
		boolean isEndOfLeftOrRightBoard = false;
		for (int i = 0; i < rightBoard.length && !isEndOfLeftOrRightBoard; i++) {
			if (!isEndOfLeftOrRightBoard) {
				if (rightOrLeft == 0) {
					if (rightBoard[i] == null) {
						isEndOfLeftOrRightBoard = true;
						rightBoard[i] = tile;
					}
				}else{
					if (leftBoard[i] == null) {
						isEndOfLeftOrRightBoard = true;
						leftBoard[i] = tile;
					}
				}
			}
		}
	}

	/**
	 * a method to return the leftboard array which contain only tiles(no null references)
	 * @return Tile array
	 */

	private Tile [] leftBoardGetter(){
		Tile [] leftBoardShrink;
		int howManyTiles = 0;
		//loop to check which size the board should be.
		for (int i = leftBoard.length - 1; i > -1; i--)
			if(leftBoard[i] !=  null)
				howManyTiles++;

		leftBoardShrink = new Tile[howManyTiles];
		for (int i = howManyTiles -1; i > -1; i--)
			leftBoardShrink[i] = leftBoard[i];

		return leftBoardShrink;
	}


	/**
	 * a method to return the rightboard array which contain only tiles(no null references)
	 * @return Tile array
	 */
	private Tile [] rightBoardGetter(){
		Tile [] rightBoardShrink;
		int howManyTiles = 0;
		//loop to check which size the board should be.
		for (int i = 0; i < rightBoard.length ; i++)
			if(rightBoard[i] !=  null)
				howManyTiles++;

		rightBoardShrink = new Tile[howManyTiles];
		for (int i =0; i < howManyTiles  ; i++)
			rightBoardShrink[i] = rightBoard[i];

		return rightBoardShrink;
	}

	/**
	 * Method to check if possible to add a tile to this board.
	 */

	private boolean isPossibleToAdd(Tile tile,int sideToAdd) {
		if (tile.getLeftNumber() < 0 || tile.getLeftNumber() > 7 ||
				tile.getRightNumber() < 0 || tile.getRightNumber() > 7)
			return false;
		int lastLeftValue, lastRightValue;
		Tile[] copyBoard = this.getBoard();
		for (int i = 0; i < copyBoard.length; i++) {
			// case when the tile is already in the board, or the board is full.
			if (copyBoard[i].equals(tile) || (i == rightBoard.length - 1 && copyBoard[i] != null))
				return false;
		}
		if (sideToAdd == 0){
			lastRightValue = this.getRightValue();
			if (tile.getLeftNumber() == lastRightValue || tile.getRightNumber() == lastRightValue)
				return true;

		}else{
			lastLeftValue = this.getLeftValue();
			if (tile.getRightNumber() == lastLeftValue || tile.getLeftNumber() == lastLeftValue)
				return true;
		}
		return false;
	}

}
