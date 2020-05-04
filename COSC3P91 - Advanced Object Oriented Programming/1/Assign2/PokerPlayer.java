package Poker;

/**
 * PokerPlayer class for Five-Card Poker
 */
public final class PokerPlayer implements Player {
    
    private final String name; // name of player
    private final Hand playerHand; // the hand a player owns
    private final int money; // how much money they possess
    
    /**
     * constructor for Player class
     * @param n - the name of the player
     */
    public PokerPlayer(String n) {
        name = n;
        money = 500; // let's say initial money is $500
        playerHand = new Hand(); // set up empty hand for player
    }
    
    /**
     * method to get the hand a player has
     * @return the player's hand
     */
    public Hand getHand() {
        return playerHand;
    }
    
    /**
     * method to get the name of the player
     * @return the name of the player
     */
    public String getName() {
        return name;
    }
    
}
