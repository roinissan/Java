/**
 * This class represent a team of players of domino game
 * @author roi nissan 
 */
public class Team {
	/**
	 * 2 fields - the team name and its players set.
	 */

	private String groupName;
	private Player [] players;


	/**
	 * Constructor with name and array of players parameters - assign these parameters to the fields.
	 * @param name
	 * @param players
	 */

	public Team(String name, Player[] players) {
		this.groupName = name;
		Player [] newPlayers;
		if(players.length ==0)
			newPlayers = new Player[0];
		else{
			newPlayers = new Player[players.length];
			for (int i = 0; i < newPlayers.length; i++) {
				newPlayers[i] = new Player(players[i]);
			}
		}
		this.players = newPlayers;
	}

	/**
	 * Copy Constructor - copy the team name and the players set.
	 */

	public Team(Team copy){
		if(copy != null){
			this.groupName = copy.groupName;
			Player [] newPlayers;
			if(copy.players == null || copy.players.length == 0)
				newPlayers = new Player[0];
			else
				newPlayers = new Player[copy.players.length];
				for (int i = 0; i < newPlayers.length; i++) {
					newPlayers[i] = new Player(copy.players[i]);
				}
			this.players = newPlayers;
		}
	}

	/**
	 * Method which returns the current players of the team.
	 * @return Player array
	 */

	public Player[] getPlayers(){
		if (players == null|| players.length == 0|| players[0] == null )
			return null;
		Player [] newPlayers = new Player[players.length];
		for (int i = 0; i < newPlayers.length; i++) {
			newPlayers[i] = new Player(players[i]);
		}
		return newPlayers;
	}

	/**
	 * Method to return the group name.
	 * @return String.
	 */

	public String getName(){
		return groupName;
	}

	/**
	 * Method to play a move of a group- starting from the first player , each player try to play a move, if he managed,
	 * then update the board and returns true, otherwise return false and the next player tries,  if all the players
	 * could not make a move then returns false.
	 * @param board
	 * @return true or false
	 */

	public boolean playMove(Board board){
		if (board == null || players == null || players.length == 0)
			return false;
		for (int i = 0; i < players.length ; i++){
			if(players[i].playMove(board))
				return true;
		}
		return false;
	}

	/**
	 * Method to check the sum of the tiles of all the players in the team.
	 * @return int number
	 */

	public int countTiles(){
		int sumOfTiles=0;
		for (int i = 0; i < players.length; i++) {
			sumOfTiles = sumOfTiles + players[i].countTiles();
		}
		return sumOfTiles;
	}

	/**
	 * Method which checks if the team has more tiles( to any player).
	 * @return true or false.
	 */

	public boolean hasMoreTiles(){
		for (int i = 0; i < players.length; i++) {
			if(players[i].hasMoreTiles())
				return true;
		}
		return false;
	}

	/**
	 * Method which returns how many players the team has.
	 * @return int value
	 */

	public int getNumberOfPlayers(){
		if (players == null)
			return 0;
		return players.length;
	}

	/**
	 * Method to assign tile to each player.
	 * @param allHands
	 * @return true or false
	 */

	public boolean assignTilesToPlayers(Tile[][] allHands){
		//isValid method  - checks to last cases which the given tiles is not valid.
		if(allHands == null || allHands.length != players.length || !isValid(allHands))
			return false;
		Tile [][] newTiles = new Tile[allHands.length][];
		for (int i = 0; i < allHands.length; i++) {
			newTiles[i] = new Tile[allHands[i].length];
			for (int j = 0; j < newTiles[i].length; j++) {
				newTiles[i][j] = new Tile(allHands[i][j]);
			}
			players[i].assignTiles(newTiles[i]);
		}
		return true;
	}

	/**
	 * toString method to create a special format of the team properties.
	 * @return string with group name and each player properties.
	 */

	@Override
	public String toString() {
		String toString = "";
		if ( players == null || players.length == 0 )
			toString = "Team: " + groupName + " {No players in the team}";
		else{
			toString = "Team: " + groupName + " {";
			for (int i = 0; i < players.length; i++) {
				if (i == players.length -1)
					toString = toString + players[i].toString() + "}";
				else
					toString = toString + players[i].toString() + ",";

			}
		}
		return toString;
	}

	/**
	 * equals method to check if given team is the same as the current team - it has to have the same name and players.
	 * @param teamToEqual
	 * @return true or false
	 */

	@Override
	public boolean equals(Object teamToEqual) {
		if( teamToEqual instanceof Team == false)
			return false;
		Team toEqual = (Team)teamToEqual;
		//isNameEquals - method to check if the names are equals.
		if(toEqual.getName().length() != groupName.length() || !isNameEquals(toEqual.groupName))
			return false;
		for (int i = 0; i < players.length; i++) {
			if(!this.getPlayers()[i].equals(toEqual.getPlayers()[i]))
				return false;
		}
		return true;
	}



	/**
	 * Method to check name equality
	 * @return true or false
	 */

	private boolean isNameEquals(String nameToEqual){
		for (int i = 0; i < nameToEqual.length(); i++) {
			if (this.groupName.charAt(i)!= nameToEqual.charAt(i))
				return false;
		}
		return true;
	}


	/**
	 * Method to check if the tile are valid
	 * @return true or false
	 */
	private boolean isValid(Tile[][] tiles){

		int totalTileSetLength = 0;
		for (int i = 0; i < tiles.length ; i++) {
			if(tiles[i] == null)
				return false;
			else{
				totalTileSetLength = totalTileSetLength + tiles[i].length;
				for(int j=0; j < tiles[i].length ; j++)
					if(tiles[i][j] == null || tiles[i][j].getLeftNumber()<0 || tiles[i][j].getLeftNumber() > 6 ||
							tiles[i][j].getRightNumber()<0 ||tiles[i][j].getRightNumber() > 6)
						return false;
			}
		}
		return isTilesEquals(tiles,totalTileSetLength);
	}

	/**
	 * Method which checks if there any duplicate tile in the tileset of all players.
	 * method operation - combine all tiles set to one array and then checks for duplication.
	 * @param tiles
	 * @param allTileSetCombineLength
	 * @return
	 */
	private boolean isTilesEquals(Tile [][] tiles,int allTileSetCombineLength){
		Tile [] allTileSetCombine = new Tile[allTileSetCombineLength];
		int tileSetIterator = 0;
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				allTileSetCombine[tileSetIterator] = tiles[i][j];
				tileSetIterator++;
			}
		}
		for (int i = 0; i < allTileSetCombine.length; i++) {
			for (int j = i; j < allTileSetCombine.length; j++) {
				if(i != j)
					if(allTileSetCombine[i].equals(allTileSetCombine[j]))
						return false;
			}
		}
		return true;
	}

}
