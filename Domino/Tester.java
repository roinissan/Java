public class Tester {

    public static Tile tile1 = new Tile(0, 6);
    public static Tile tile2 = new Tile(1, 2);
    public static Tile tile3 = new Tile(5, 5);
    public static Tile tile4 = new Tile(3, 5);
    public static Tile tile5 = new Tile(3, 4);
    public static Tile tile6 = new Tile(6, 3);
    public static Tile tile7 = new Tile(2, 1);
    public static Tile tile8 = new Tile(1, 3);
    public static Tile tile9 = null;

    public static void TileChecker() {
        System.out.print(tile1.getLeftNumber()+",");
        System.out.print(tile1.getRightNumber()+",");
        System.out.print(tile7.getLeftNumber()+",");
        System.out.println(tile1.getRightNumber());
        System.out.println(tile5);
        System.out.println(tile4);
        tile4.flipTile();
        System.out.println(tile4);
        System.out.println(tile1.equals(tile1));
        System.out.println(tile5.equals(tile3));
        System.out.println(tile2.equals(tile7));
    }

    public static void BoardChecker(){
        Board board1 = new Board(6);
        Board board2 = new Board(8);
        System.out.println(board1);
        System.out.println(board1.getLeftValue());
        System.out.println( board2.getRightValue());
        System.out.println(board1.equals(board2));
        if (board1.getBoard() == null)
            System.out.println("null");
        System.out.println(board1.addToRightEnd(tile7));
        System.out.println(board1.addToRightEnd(tile2));
        System.out.println(board1.addToRightEnd(tile9));
        System.out.println(board1.addToRightEnd(tile8));
        System.out.println(board1.addToRightEnd(tile5));
        System.out.println(board2.addToRightEnd(tile7));
        System.out.println(board2.addToRightEnd(tile2));
        System.out.println(board2.addToRightEnd(tile8));
        System.out.println(board2.addToRightEnd(tile6));
        System.out.println(board1);
        System.out.println(board2);
        System.out.println(board1.equals(board2));

    }

    public static void PlayerChecker(){
        Tile [] tiles = {tile1,tile3};
        Tile [] tiles1 = {};
        Tile [] tiles2 = {tile1,tile7};
        Tile [] tiles3 = {tile5,tile6};
        Tile [] tiles4 = {tile3,tile7,tile1};
        Player player1 = new Player("player1",tiles);
        //Player player2 = new Player("player2");
        //Player player4 = new Player("player1",tiles4);
        //player2.assignTiles(tiles1);
        //System.out.println(player2.hasMoreTiles());
        //System.out.println(player2);
        //System.out.println(player2.assignTiles(tiles3));
       // System.out.println(player2);
       // player2.assignTiles(tiles2);
        System.out.println(player1.countTiles());
        System.out.println(player1);
        System.out.println(player1.assignTiles(tiles2));
        System.out.println(player1);
       // System.out.println(player4);
       // System.out.println(player1.equals(player2));
       // System.out.println(player1.equals(player4));

    }

    public static void TeamCheacker(){
        Tile [] tiles = {tile1,tile3,tile8};
        Tile [] tiles1 = {};
        Tile [] tiles2 = {tile1,tile3,tile7};
        Tile [] tiles3 = {tile5,tile6};
        Tile [] tiles4 = {tile3,tile7,tile1};
        Tile [][] tiless = {tiles,tiles3};
        Tile [][] tiless1 = {tiles,tiles4};
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player [] players = new Player[2];
        players[0] = player1;
        players[1] = player2;
        Team team1 = new Team("team1",players);
        System.out.println(team1);
        System.out.println(team1.assignTilesToPlayers(tiless));
        System.out.println(team1);
        System.out.println(team1.assignTilesToPlayers(tiless1));
        System.out.println(team1);


    }


    public static void main(String[]args){
        //Tester.TileChecker();
        // Tester.BoardChecker();
        //Tester.PlayerChecker();
        Tester.TeamCheacker();
    }
}
