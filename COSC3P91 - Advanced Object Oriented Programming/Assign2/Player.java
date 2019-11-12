package Poker;

/**
 * Player interface for Five-Card Poker
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
