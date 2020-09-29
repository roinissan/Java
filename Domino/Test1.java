/*
import java.util.LinkedList;

public class Test1 {


    public static void main(String[] args) {
        LinkedList <Integer> x = new LinkedList<Integer>();
        x.add()
     */
/*   Tile dom1 = new Tile(5,1); // 5 | 1
        Tile dom2 = new Tile(6,4); // 6 | 4
        Tile dom3 = new Tile(3,0); // 3 | 0
        Tile dom4 = new Tile(3,0); // 0 | 3

		*//*
*/
/* Print the dominos *//*
*/
/*
		*//*
*/
/* Test the toString() method *//*
*/
/*
        System.out.println("This domino is " + dom1);
        System.out.println("This domino is " + dom2);
        System.out.println("This domino is " + dom3);

		*//*
*/
/* Get the L/R values *//*
*/
/*
        System.out.println();
        System.out.println("The Right value for " + dom1 + " is " + dom1.getRightNumber());
        System.out.println("The Left value for " + dom2 + " is " + dom2.getLeftNumber());
        System.out.println("The Right value for " + dom3 + " is " + dom3.getRightNumber());

		*//*
*/
/* Test the equals() method *//*
*/
/*
        System.out.println();
        System.out.println("Are " + dom1 + " and " + dom1 + " are the same? " + dom1.equals(dom1)); // true
        System.out.println("Are " + dom2 + " and " + dom3 + " are the same? " + dom2.equals(dom3)); // false
        System.out.println("Are " + dom3 + " and " + dom4 + " are the same? " + dom3.equals(dom4)); // true


        System.out.println("-- Testing the Player & Team Classes --");
        System.out.println();

	    *//*
*/
/* Make the players for testing *//*
*/
/*
        Player Ali = new Player("Ali");
        Player Tabib = new Player("Tabib");
        Player [] players = {Ali, Tabib};
        Team Beitar = new  Team("Beitar", players);
        Player Barak = new Player("Barak", null);
        Player Bachar = new Player("Bachar", null);
        Player [] players2 = {Barak, Bachar};
        Team Hapoel = new  Team("Hapoel", players2);
        Player Jordi = new Player("Jordi", null);
        Player Kroif = new Player("Kroif", null);
        Player [] players3 = {Jordi, Kroif};
        Team Maccabi = new  Team("Maccabi", players3);

		*//*
*/
/* Should be 2 players now *//*
*/
/*
        System.out.println("There are " + Beitar.getNumberOfPlayers() + " players at "+Beitar.getName());
        System.out.println("There are " + Hapoel.getNumberOfPlayers() + " players at "+Hapoel.getName()+"\n");


		*//*
*/
/* Make the dominos for testing *//*
*/
/*

        Tile dom5 = new Tile(5,1); // 5 | 5
        Tile dom6 = new Tile(6,4); // 6 | 4
        Tile dom7 = new Tile(3,2); //  3 | 2
        Tile dom8 = new Tile(0,4); // 0 | 4
        Tile dom9 = new Tile(5,6); // 5 | 6
        Tile dom10 = new Tile(3,0); // 3 | 0
        Tile dom11 = new Tile(0,5); // 0 | 5
        Tile [] AliHand= {dom5,dom8,dom10};
        Tile [] TabibHand= {dom6,dom7,dom9};

		*//*
*/
/* -- Testing the assignTiles() method -- *//*
*/
/*
		*//*
*/
/* Add dominos to Tabib hand *//*
*/
/*
        Tabib.assignTiles(TabibHand);
		*//*
*/
/* Add dominos to Ali hand *//*
*/
/*
        Ali.assignTiles(AliHand);
		*//*
*/
/* Print players info *//*
*/
/*
        System.out.println(Tabib.toString());
        System.out.println(Ali.toString());


		*//*
*/
/* The hands should be full *//*
*/
/*
        System.out.println();
        System.out.println("Is Ali hand full? " + Ali.hasMoreTiles()); // should be true;
        System.out.println("Is Tabib  hand full? " + Tabib.hasMoreTiles()); // should be true;

		*//*
*/
/* The value of the hands *//*
*/
/*
        System.out.println();
        System.out.println(" Ali counts " + Ali.countTiles()); // should be 13
        System.out.println( "Tabib counts "+ Tabib.countTiles()); // should be 26
        // OPTIONAL OPTIONAL OPTIONAL OPTIONAL
		*//*
*/
/* Remove dominos from Tabib hand *//*
*/
/*
        //remove(TabibHand, dom6);
        //remove(TabibHand, dom7);
		*//*
*/
/* Reprint Tabib's updated hand : one dom left*//*
*/
/*
        System.out.println();
        System.out.println(Tabib.toString()); //<5 6>
        //remove(TabibHand, dom9);
        System.out.println(Tabib.toString()); // []
        System.out.println("Is Tabib  hand full? " + Tabib.hasMoreTiles()+"\n"); // false

        Board play= new Board(28);

		*//*
*/
/* -- Testing the addToL/REnd() method -- *//*
*/
/*
        play.addToLeftEnd(dom9);//   5 | 6
        play.addToRightEnd(dom6);// 6 | 4
        play.addToRightEnd(dom8);// 0 | 4
        System.out.println("Board play : "+play.toString()+"\n");    // Should be  <5,6>,<6,4>,<4,0>
        System.out.println(" Create a empty list for the domino pack");
		*//*
*/
/* Create a empty list for the domino pack *//*
*/
/*
        Tile [] pack = new Tile[28];
        int loop=0;
		*//*
*/
/* 2-D Loop that creates the 28 dominoes *//*
*/
/*
        for(int i = 0; i <= 6; i++)
            for(int k = i; k <= 6; k++) {
                Tile newDom = new Tile(i,k);
                pack[loop]=newDom;
                loop++;
            }
		*//*
*/
/* Print the 28 pack *//*
*/
/*
        int printCount = 0;
        for(Tile dom : pack){

			*//*
*/
/* Print the domino *//*
*/
/*
            System.out.print(dom +" ");
            printCount++;

			*//*
*/
/* Make a new line after 7 dominos have been printed *//*
*/
/*
            if(printCount > 6){
                System.out.println();
                printCount = 0;
            }
        }
        System.out.println("\n  Print the new shuffled pack");
        // shuffle the pack - OPTIONAL OPTIONAL OPTIONAL OPTIONAL
        //shufflePack(pack);
		*//*
*/
/* Print the new shuffled pack *//*
*/
/*
        printCount = 0;
        for(Tile dom : pack){

			*//*
*/
/* Print the domino *//*
*/
/*
            System.out.print(dom +" ");
            printCount++;

			*//*
*/
/* Make a new line after 7 dominos have been printed *//*
*/
/*
            if(printCount > 6){
                System.out.println();
                printCount = 0;
            }
        }
        System.out.println();
        Board B= new Board(10);
        System.out.println(B.getRightValue()); // -1
        System.out.println(B.getLeftValue()); // -1
        B.addToRightEnd(dom6);// 6 | 4 to right
        System.out.println(B.toString());//<6,4>
        System.out.println(B.getRightValue()); //4
        System.out.println(B.getLeftValue()); // 6
        B.addToRightEnd(dom8);// 4 | 0 to right
        System.out.println(B.toString()); //<6,4>,<4,0>
        System.out.println(B.getRightValue());//0
        System.out.println(B.getLeftValue());//6
        B.addToRightEnd(dom11);//5 | 0 to right
        System.out.println(B.toString());// <6,4>,<4,0>,<0,5>
        System.out.println(B.getLeftValue());//6
        System.out.println(B.getRightValue());//5
        B.addToLeftEnd(dom9); // 5 | 6 to left
        System.out.println(B.toString()); //<5,6>,<6,4>,<4,0>,<0,5>
        System.out.println(B.getLeftValue());//5
        System.out.println(B.getRightValue());//5
        B.addToLeftEnd(dom2); // *cant add this dom!!!*
        System.out.println(B.toString()); //<5,6>,<6,4>,<4,0>,<0,5>
        B.addToLeftEnd(dom1); // will add this dom 5 | 1 to left
        System.out.println(B.toString()); //<1,5>,<5,6>,<6,4>,<4,0>,<0,5>

        GameManager game = new GameManager(Maccabi, Hapoel, 4);
        System.out.println(game.play());
        System.out.println(game.toString());

        // count score for each team
        System.out.println("Maccabi score is: "+Maccabi.countTiles()
                +"; and they got more tiles? "+Maccabi.hasMoreTiles());// score : true
        System.out.println("Hapoel score is: "+Hapoel.countTiles()
                +"; and they got more tiles? "+Hapoel.hasMoreTiles()+"\n"); // score : ture


        System.out.print("  ๏̯͡๏﴿,");*//*





        Player roi = new Player("roi");
        Player yoni = new Player("Roi");
        System.out.println(roi.equals(yoni));
        Player [] players3 = {roi,yoni};
        Team hapoel = new Team ("Hapoel",players3);
        Player aviv = new Player ("Aviv");
        Player tomeer =new Player ("Tomer");
        Player [] Player4 = {aviv,tomeer};
        Team maccabi = new Team("Maccabi",Player4);
        GameManager game = new GameManager(maccabi,hapoel,5);
        //System.out.println(game.play());
        System.out.println(game);
        System.out.println("");
    }
}
*/
