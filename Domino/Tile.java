/**
 * 	This class represent a tile of a domino game.
 * @author roi nissan 
 */

public class Tile {
	/**
	 * two fields which represent the value of domino tile in both ends - left and right.
	 */

	private int leftNumber;
	private int rightNumber;

	/**
	 * Constructor with 2 parameters left and right number - to assign the numbers of a tile
	 * @param leftNumber
	 * @param rightNumber
	 */

	public Tile(int leftNumber, int rightNumber) {
		this.leftNumber = leftNumber;
		this.rightNumber = rightNumber;
	}

	/**
	 * A copy constructor - receives a tile object and copy its fields.
	 * @param Tile object
	 */

	public Tile(Tile copy){
		if(copy != null){
			this.rightNumber = copy.getRightNumber();
			this.leftNumber = copy.getLeftNumber();
		}else{
			this.leftNumber = 0;
			this.rightNumber = 0;
		}
	}
	
	/**
	 * Method to return the left number of the tile.
	 * @return leftNumber
	 */

	public int getLeftNumber() {
		return leftNumber;
	}

	/**
	 * Method to return the right number of the tile.
	 * @return rightNumber
	 */

	public int getRightNumber() {
		return rightNumber;
	}

	/**
	 * Method to switch the positions of the right and left number in a tile.
	 */

	public void flipTile() {
		int temp = leftNumber;
		this.leftNumber = rightNumber;
		this.rightNumber = temp;
		
	}

	/**
	 * toString method to create a special format to the tile.
	 * @return string with the left and right number of the tile.
	 */

	@Override
	public String toString() {
		String printString = "<" + leftNumber + "," + rightNumber + ">";
		return printString;
	}

	/**
	 * equals method to check if given tile is the same as the current tile (flipped tiles also counts as equal).
	 * @param tileToCheck of object Tile
	 * @return true or false.
	 */

	@Override
	public boolean equals(Object tileToCheck) {
		if(!(tileToCheck instanceof Tile))
			return false;
		Tile toCheck = (Tile)tileToCheck;
		if ((rightNumber == toCheck.getRightNumber() && leftNumber == toCheck.getLeftNumber()) ||
				(rightNumber == toCheck.getLeftNumber() && leftNumber == toCheck.getRightNumber()))
			return true;
		return false;

	}
	
}
