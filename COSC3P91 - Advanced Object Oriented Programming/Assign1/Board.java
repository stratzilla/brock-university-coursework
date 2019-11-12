package FivePoker;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Board class for Five-Card Poker
 */
public final class Board {
    
    private static final String[] HANDS = {"High Card","One Pair","Two Pairs",
                                        "Three of a Kind","Straight","Flush",
                                        "Full House","Four of a Kind",
                                        "Straight Flush","Royal Flush"};
    private final ArrayList<Player> players;
    private Deck d;
    private Tiebreaker t;
    
    /**
     * constructor for the Board class
     */
    public Board () {
        // set up players
        players = new ArrayList<>();
        addPlayer("Andrew"); // test player
        addPlayer("Meaghan"); // test player
        setUp(); // set up the board initial conditions
        printState(); // print current state
        gameplay();
        findWinner(); // find winner
    }
    
    /**
     * method to simulate gameplay; works with multiple players
     */
    private void gameplay() {
        d.burn(); // burn card as instructed
        int numRepopulate = 0; // for repopulating hand later
        Scanner in = new Scanner(System.in); // for input
        for (Player p : players) { // for each player
            int key = -2; // placeholder
            System.out.println("\nPlayer " + p.getName() + "'s turn.");
            System.out.println("\nWhich cards would you like to redraw?");
            System.out.println("1-5 from left to right. '0' when done.\n");
            while (key != -1 && p.getHand().getSize() > 0) {
                key = in.nextInt()-1;
                /**
                 * as long as user input is valid (ie not outside bounds of
                 * 1..handSize and user input isn't 0), discard cards
                 */
                if (key < -1 || key > p.getHand().getSize()-1) {
                    System.out.println("That is an invalid number!\n");
                } else if (key != -1) {
                    numRepopulate++;
                    p.getHand().discardCard(key);
                    p.getHand().showHand(); // show hand after discarding
                }
            }
            /**
             * repopulate hand with missing cards; a new score is tabulated
             * once the handsize becomes 5 again
             */
            for (int i = 0; i < numRepopulate; i++) {
               d.drawTo(p); // draw to that player
            }
            System.out.print("\n" + p.getName() + "'s new hand is: ");
            p.getHand().showHand(); // show their new hand
        }
    }
    
    /**
     * this'll be used another time when GUI is used
     */
    private void addPlayer() {}
    
    /**
     * method which adds a player to the game
     * @param s - the player's name
     */
    private void addPlayer(String s) {
        Player p = new Player(s);
        players.add(p); // add 'em
    }
    
    /**
     * method to setup initial conditions of board
     */
    private void setUp() {
        d = new Deck(); // set deck on board
        t = new Tiebreaker(); // create a 'referee'
        // for each player, set initial hand conditions
        for (Player p : players) {
            d.drawInitial(p);
        }
    }
    
    /**
     * method to print the board state
     */
    private void printState() {        
        for (Player p : players) {
            p.printName(); // print player name
            p.getHand().showHand(); // print their hands
            String score = ""; // truncated to two lines 'cause ugly
            score = HANDS[p.getHand().getScore()-1];
            System.out.println(score+"\n");
        }
    }
    
    /**
     * method which finds a winner by tiebreaker if necessary
     */
    private void findWinner() {
        Player tempWinner = players.get(0); // initially assume
        for (int i = 1; i < players.size(); i++) {
            // find highest scored winner if multiple
            tempWinner = t.findWinner(tempWinner, players.get(i));
        }
        // print it!
        System.out.println("Winner is: " + tempWinner.getName());
    }
}
