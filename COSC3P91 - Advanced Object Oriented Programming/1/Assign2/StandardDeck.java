package Cards;

import java.util.ArrayList;
import java.util.Collections;

/**
 * StandardDeck class for Five-Card poker, implements Deck interface
 */
public final class StandardDeck implements Deck {
    
    private ArrayList<Card> theDeck; // a collection of cards representing deck
    
    /**
     * constructor for Deck object; populates deck with cards in order
     * and then shuffles it to a random order
     */
    public StandardDeck() {
        initializeDeck();
        shuffleDeck();
    }
    
    /**
     * method which sets initial condition for a deck
     */
    public void initializeDeck() {
        theDeck = new ArrayList<>(); // init deck
        // initially populate deck with cards in order
        for (int i = 0; i < 52; i++) { // for each rank and face
            Card aCard = new Card(i); // init new card of specs
            theDeck.add(aCard); // add to deck
        }
    }
    
    /**
     * method which shuffles the deck
     */
    public void shuffleDeck() {
        Collections.shuffle(theDeck); // prebaked method
    }
    
    /**
     * method which returns the current deck size
     * @return the deck size
     */
    public int getDeckSize() {
        return theDeck.size();
    }
    
    /**
     * method which draws a card from the deck if deck is not empty
     * @throws IllegalArgumentException if deck is empty
     * @return the top card from the deck
     */
    public Card draw() {
        if (getDeckSize() == 0) {
            throw new IllegalArgumentException("The deck is currently empty!");
        }
        return theDeck.remove(0);
    }
    
}