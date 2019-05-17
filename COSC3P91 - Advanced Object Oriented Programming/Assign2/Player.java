package Poker;

/**
 * Player interface for Five-Card Poker
 * perhaps in the future Dealer can become player? Might be interesting and
 * would necessitate the interface
 * @author Robert Scott - 2018
 */
public interface Player {
    
    /**
     * method to get the hand a player has
     * @return the player's hand
     */
    public Hand getHand();
    
    /**
     * method to get the name of the player
     * @return the name of the player
     */
    public String getName();
}
