/**
 * This class represent a player of domino game
 * @author roi nissan 
 */

public class  Player {
	/**
	 * 2 fields - the player name and its tiles set.
	 */

	private String playerName;
	private Tile [] tilesSet;

	/**
	 * Constructor which receives 2 parameters - name and tiles, and then assign values to the fields.
	 * @param name
	 * @param tiles
	 */

	public Player(String name, Tile[] tiles) {
		this.playerName = name;
		this.assignTiles(tiles);
	}

	/**
	 * Constructor with name parameter - assign the name to the playerName field.
	 * @param name
	 */

	public Player(String name) {
		this.playerName = name;
		this.tilesSet = new Tile[0];
	}

	/**
	 * Copy constrctor - copy the player name and its tiles set.
	 * @param copy
	 */
	public Player(Player copy){
		if ( copy!= null){
			this.playerName = copy.playerName;
			if(copy.tilesSet !=null && copy.tilesSet.length>0) {
				this.tilesSet = new Tile[copy.tilesSet.length];
				for (int i = 0; i < tilesSet.length; i++) {
					this.tilesSet[i] = new Tile(copy.tilesSet[i]);
				}
			}else
				this.tilesSet = new Tile[0];
		}
	}

	/**
	 * Method to assign tiles to a player , returns false if the input isn't valid, otherwise assign and returns true.
	 * @param tiles
	 * @return true or false
	 */

	public boolean assignTiles(Tile[] tiles) {
		//isValid method  - checks to last cases which the given tiles is not valid.
		if (tiles == null || tiles.length > 28 || !isValid(tiles))
			return false;
		Tile [] copy;
		if (tiles.length == 0)
			copy = new Tile[0];
		else {
			copy = new Tile[tiles.length];
			for (int i = 0; i < copy.length; i++) {
				copy[i] = new Tile(tiles[i]);
			}
		}
		this.tilesSet = copy;
		return true;
	}

	/**
	 * Method to play one move of a player- first,check if the board is empty, if it does, put the first tile of the player in the board and then remove that tile from its tile set.
	 * if the board is not the empty,check if the player has a tile which could be assign to the left  side of the board or to the right(first the left),
	 * if it does then updates the board and remove the tile from the set of the player, and return true, if the player does not have any tile which fits, return false.
	 * @param board
	 * @return true or false
	 */
	public boolean playMove(Board board) {
		boolean isUpdate = false,isNull = false;
		Tile [] newTile; // new tile set to case when the board updated.
		if(board==null || tilesSet == null || tilesSet.length==0)
			return false;
		newTile = new Tile[tilesSet.length-1];
		int tileToRemove = -1;
		//check if the board empty
		if(board.getBoard() == null && tilesSet.length > 0 ){
			board.addToRightEnd(tilesSet[0]);
			tileToRemove = 0;
			isUpdate =true;
		}else{
			//iterates till the first tile which can fit(to either side - left side is checked first).
			for (int i = 0; i < tilesSet.length && !isUpdate; i++) {
				if(tilesSet[i] != null){
					if (board.addToLeftEnd(tilesSet[i])) {
						isUpdate = true;
						tileToRemove = i;
					}else if(board.addToRightEnd(tilesSet[i])) {
						isUpdate = true;
						tileToRemove = i;
						}
					}
				}
			}
			//updates the player set if it updated the board(remove).
			if (isUpdate) {
				int loop =0;
				for (int i = 0; i < tilesSet.length && !isNull; i++) {
					if(i != tileToRemove){
						newTile[loop] = this.tilesSet[i];
						loop++;
					}
				}
				this.assignTiles(newTile);
			}
		return isUpdate;
	}

	/**
	 * Method to count the sum of all the tiles that the player currently has.
	 * @return int number
	 */

	public int countTiles() {
		int sumOFTiles = 0;
		if(tilesSet != null){
			for (int i = 0; i < tilesSet.length ; i++) {
				if (tilesSet[i] != null)
					sumOFTiles = sumOFTiles + tilesSet[i].getRightNumber() + tilesSet[i].getLeftNumber();
			}
		}
		return sumOFTiles;
	}

	/**
	 * Method which checks if the player has any more tiles.
	 * @return true or false
	 */

	public boolean hasMoreTiles() {
		if (tilesSet == null || tilesSet.length == 0 ||tilesSet[0] == null)
			return false;
		return true;
	}

	/**
	 * toString method to create a special format of the player properties.
	 * @return string with all the player name and its tiles.
	 */

	@Override
	public String toString() {
		String toString = "";
		if (tilesSet== null || tilesSet.length ==0 ||tilesSet[0] == null){
			toString = playerName + ":[]";
		}else{
			toString = playerName + ":[";
			for (int i = 0; i < tilesSet.length ; i++) {
				if( i == tilesSet.length -1)
					toString = toString + tilesSet[i].toString() + "]" ;
				else
					toString = toString + tilesSet[i].toString() + ",";
			}
		}
		return toString;
	}


	/**
	 * equals method to check if given player is the same as the current player - it has to have the same name and tiles.
	 * @param playerToEqual
	 * @return true or false
	 */

	@Override
	public boolean equals(Object playerToEqual) {
		if(playerToEqual instanceof Player == false)
			return false;
		Player toEqual = (Player)playerToEqual;
		//isNameEquals - method to check if the names are equals.
		if(toEqual.playerName.length() != playerName.length() || (toEqual.tilesSet == null && tilesSet != null) ||
		(tilesSet == null && toEqual.tilesSet != null) || tilesSet.length != toEqual.tilesSet.length || !isNameEquals(toEqual.playerName))
			return false;
		//case when both tilesSet is null.
		if(toEqual.tilesSet == null)
			return true;
		boolean isContain = false;
		for (int i = 0; i < tilesSet.length; i++) {
			for (int j = 0; j < tilesSet.length && !isContain; j++) {
				if (tilesSet[i].equals(toEqual.tilesSet[j]))
					isContain = true;
			}
			if (!isContain)
				return false;
		}
		return true;
	}

	/**
	 * Method to check if array of tiles is valid;
	 * @param tiles arr.
	 * @return true or false.
	 */
	private boolean isValid(Tile[] tiles){
		for (int i = 0; i < tiles.length; i++) {
			if(tiles[i] == null || tiles[i].getLeftNumber()<0 || tiles[i].getLeftNumber() > 6 ||
					tiles[i].getRightNumber()<0 ||tiles[i].getRightNumber() > 6)
				return false;
		}
		return isTilesEquals(tiles);
	}

	/**
	 * Method to check if there repetition in the tiles set.
	 * @param tiles arr
	 * @return true or false
	 */
	private boolean isTilesEquals(Tile [] tiles){
		for (int i = 0; i < tiles.length; i++) {
			for (int j = i; j < tiles.length; j++) {
				if(j != i)
					if(tiles[i].equals(tiles[j]))
						return false;
			}
		}
		return true;
	}

	/**
	 * Method to check name equality
	 * @return true or false
	 */
	private boolean isNameEquals(String nameToEqual){
		for (int i = 0; i < nameToEqual.length(); i++) {
			if (this.playerName.charAt(i)!= nameToEqual.charAt(i))
				return false;
		}
		return true;
	}

}
