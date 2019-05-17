package Main;

import Cards.Card;
import Poker.Dealer;
import Poker.Hand;
import Poker.Player;
import Poker.PokerPlayer;
import Poker.Tiebreaker;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * GameMaster class for Five-Card Poker
 * @author Robert Scott - 2018
 */
public final class GameMaster {
    
    /**
     * again I should use enums but without an easy way to access them, such
     * as in an array, this makes more sense to me. See my other comment on
     * enums or in the document for more verbose explanation.
     */
    private static final String[] HANDS = {"High Card","One Pair","Two Pairs",
                                        "Three of a Kind","Straight","Flush",
                                        "Full House","Four of a Kind",
                                        "Straight Flush","Royal Flush"};
    private final ArrayList<PokerPlayer> players; // the players
    private final Dealer dealer; // the dealer
    private final Tiebreaker tiebreaker; // a tiebreaker if necessary
    
    /**
     * constructor for the Board class
     */
    public GameMaster () {
        // add dealer to game
        dealer = new Dealer();
        // add tiebreaker to game
        tiebreaker = new Tiebreaker();
        // add player(s) to game
        players = new ArrayList<>();
    }
    
    /**
     * method to initially set up the game board
     * used to add players, burn card, deal initial hands, etc
     */
    private void setupGame() {
        addPlayer("Andrew"); // test player
        addPlayer("Meaghan"); // test player
        dealer.burnCard();
        for (PokerPlayer p : players) {
            dealer.dealHand(p);
        }
    }
    
    /**
     * method to simulate gameplay; works with multiple players
     */
    protected void gameplay() {
        setupGame(); // set it up first
        Scanner in = new Scanner(System.in); // for input
        for (PokerPlayer p : players) { // for each player
            Hand playerHand = p.getHand(); // grab the player's hand
            // print some info; will move to GUI eventually
            System.out.println("\nPlayer " + p.getName() + "'s turn.");
            System.out.print("Initioal Hand: ");
            System.out.println(playerHand.showHand());
            boolean[] boolArray = new boolean[5]; // interesting
            int i = 0;
            for (Card c : playerHand.getHand()) {
                String choice;
                while (true) {
                    System.out.print("Keep " + c.getCard() + "? (Y/N): ");
                    choice = in.next();
                    if (choice.equalsIgnoreCase("Y")) {
                        boolArray[i] = false;
                        break;
                    } else if (choice.equalsIgnoreCase("N")) {
                        boolArray[i] = true;
                        break;
                    } else {
                        System.out.println("Invalid answer.");
                    }
                }
                i++;
            }
            dealer.redrawCard(p, boolArray); // dealer redraws cards to player
            System.out.println("Final Hand: " + p.getHand().showHand());
            System.out.print(p.getName());
            String playerScore = HANDS[p.getHand().getScore().getVal()-1];
            System.out.println("'s hand has "+playerScore);
        }
        findWinner(); // find winner
    }
    
    /**
     * method which adds a player to the game
     * @param s - the player's name
     */
    private void addPlayer(String s) {
        Player p = new PokerPlayer(s);
        players.add((PokerPlayer) p); // add 'em
    }
    
    /**
     * method which finds a winner by tiebreaker if necessary
     * eventually moving to GUI
     */
    private void findWinner() {
        PokerPlayer tempWinner = players.get(0); // initially assume
        for (int i = 1; i < players.size(); i++) {
            // find highest scored winner if multiple
            tempWinner = tiebreaker.findWinner(tempWinner, players.get(i));
        }
        // print it
        System.out.println("\nWinner is: " + tempWinner.getName() + "!");
    }
}
