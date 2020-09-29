/**
 * This class represent a game manger of domino game
 * @author roi nissan 
 */

public class GameManager {
	/**
	 * 3 fields - 2 teams and the board of the game.
	 */

	private Team team1;
	private Team team2;
	private Board board;

	/**
	 * Constructor which receives the two teams which  already assigned tiles to their players,
	 * initiate the game fields
	 * @param team1
	 * @param team2
	 */

	public GameManager(Team team1, Team team2){
		this.team1 = new Team(team1);
		this.team2 = new Team(team2);
		this.board = new Board(28);
	}

	/**
	 * Constructor which receives the two teams and how many tiles each player should get.
	 * @param team1
	 * @param team2
	 * @param tilesPerPlayer
	 */

	public GameManager(Team team1, Team team2, int tilesPerPlayer) {
		this.team1 = new Team(team1);
		this.team2 = new Team(team2);
		this.board = new Board((team1.getNumberOfPlayers()+team2.getNumberOfPlayers())*tilesPerPlayer);
		dealTiles(tilesPerPlayer);
	}

	/**
	 * This method starts the game between the teams,first team starts.
	 * if a team has no more tiles or both does not have fitting tile, the game is finished.
	 * then check which team has less sum of tiles and returns the winner in a string
	 *
	 * @return String with all the content of the game
	 */

	public String play() {
		int team1Score,team2Score,passCounter = 0;
		boolean isTeamSucceeded,isTeamFinished = false;
		String scoreString = "";
		//whose turn - boolean value to identify which team should play
		for (int i = 0; i < 28 && !isTeamFinished; i++) {
			// end if one team or both have no more tiles.
			if (passCounter >= 2 || !team1.hasMoreTiles() || !team2.hasMoreTiles())
				isTeamFinished = true;
			else{
				isTeamSucceeded = team1.playMove(this.board);
				if(isTeamSucceeded){
					scoreString = scoreString+team1.getName() + ", success: " + board.toString() + "\n" ;
					passCounter = 0;
				}else{
					scoreString = scoreString +team1.getName() + ", pass: " + board.toString() + "\n" ;
					passCounter++;
				}

				isTeamSucceeded = team2.playMove(this.board);
				if (isTeamSucceeded) {
					scoreString = scoreString+team2.getName() + ", success: " + board.toString() + "\n";
					passCounter = 0;
				} else {
					scoreString = scoreString +team2.getName() + ", pass: " + board.toString() + "\n";
					passCounter++;
				}
			}
		}
		team1Score = team1.countTiles();
		team2Score = team2.countTiles();
		scoreString = scoreString + team1.getName() + ", score: " + team1Score + "\n" + team2.getName() + ", score: " + team2Score + "\n";
		if (team1Score > team2Score)
			scoreString = scoreString + team2.getName() + " wins\n";
		else if (team2Score > team1Score)
			scoreString = scoreString + team1.getName() + " wins\n";
		else
			scoreString = scoreString + "Draw! - the house wins\n";
		return scoreString;
	}

	/**
	 * toString method which returns the game properties
	 * @return String
	 */

	@Override
	public String toString() {
		String toStringTeam1 = team1.toString() + "\n";
		String toStringTeam2 = team2.toString();
		return toStringTeam1 + toStringTeam2;
	}

	/**
	 * check if the two games have the same teams in the same order.
	 * @param gameToEqual
	 * @return true or false
	 */

	@Override
	public boolean equals(Object gameToEqual) {
		if (gameToEqual instanceof GameManager == false)
			return false;
		GameManager toEqual = (GameManager)gameToEqual;
		if(toEqual.team1.equals(this.team1) && toEqual.team2.equals(this.team2))
			return true;
		return false;
	}

	/**
	 * Method to assign tiles to each player in a team, the amount of tiles is based on the parameter.
	 * first create tileSet (using createTiles method) which contains all the possible tile(28),
	 * then shuffle the tiles order with math.random method(using shuffle method).
	 * assign tile set in length of numberOfTiles to each player, then assign the teams set.
	 * @param numberOfTiles
	 */

	private void dealTiles(int numberOfTiles) {
		Tile [] tilesSet = createTiles();
		shuffle(tilesSet);
		int numOfPlayersTeam1 = team1.getNumberOfPlayers(), numOfPlayersTeam2 = team2.getNumberOfPlayers();
		Tile[][] team1TileSet = new Tile[numOfPlayersTeam1][], team2TileSet = new Tile[numOfPlayersTeam2][];
		Tile[] playerTileSet = new Tile[numberOfTiles];
		int loopIterator = 0;
		for (int i = 0; i < numOfPlayersTeam1 + numOfPlayersTeam2; i++) {
			for (int j = 0; j < numberOfTiles; j++) {
				playerTileSet[j] = tilesSet[loopIterator];
				loopIterator++;
			}
			if (i <numOfPlayersTeam1 )
				team1TileSet[i] = playerTileSet;
			else
				team2TileSet[i - numOfPlayersTeam1] = playerTileSet;
			playerTileSet = new Tile[numberOfTiles];
		}
		team1.assignTilesToPlayers(team1TileSet);
		team2.assignTilesToPlayers(team2TileSet);
	}

	/**
	 * Method to create all the 28 tiles in domino game;
	 * @return Tile array
	 */

	private Tile[] createTiles() {
		Tile[] tilesSet = new Tile[28];
		int loopIterator = 0;
		for (int i = 0; i < 7; i++) {
			for (int j = i; j < 7; j++) {
				tilesSet[loopIterator] = new Tile(i, j);
				loopIterator++;
			}
		}
		return tilesSet;
	}

	/**
	 * Method to shuffle tiles array - used after createTiles.
	 * @param tilesSet
	 */

	private void shuffle(Tile [] tilesSet){
		int changeIndex;
		Tile tempTileContainer;
		for (int i = 0; i < tilesSet.length; i++) {
			changeIndex = (int)(Math.random()*27);
			tempTileContainer = tilesSet[i];
			tilesSet[i] = tilesSet[changeIndex];
			tilesSet[changeIndex] = tempTileContainer;
		}
	}

}
