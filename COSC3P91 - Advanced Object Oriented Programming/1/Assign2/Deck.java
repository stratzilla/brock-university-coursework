package Cards;

/**
 * Deck interface for Five-Card Poker. 
 * @author Riista
 */
public interface Deck {
    
    /**
     * method to initially set up a deck; implementing class determines
     * deck size, etc
     */
    public void initializeDeck();

    /**
     * method to shuffle the deck
     */
    public void shuffleDeck();

    /**
     * method which gives the deck size
     * @return the deck size
     */
    public int getDeckSize();
    
    /**
     * method which draws a card
     * @return a card object
     */
    Card draw();
    
}
