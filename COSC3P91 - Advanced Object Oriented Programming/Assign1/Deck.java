package FivePoker;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Deck class for Five-Card poker
 */
public final class Deck {
    
    private static final int DECKSIZE = 52; // standard 52-card deck
    private ArrayList<Card> theDeck; // a collection of cards representing deck
    
    /**
     * constructor for Deck object; populates deck with cards in order
     * and then shuffles it to a random order
     */
    Deck() {
        setUpDeck();
    }
    
    /**
     * method which sets initial condition for a deck
     */
    private void setUpDeck() {
        theDeck = new ArrayList<>(); // init deck
        // initially populate deck with cards in order
        for (int i = 0; i < DECKSIZE; i++) { // for each rank and face
            Card aCard = new Card(i); // init new card of specs
            theDeck.add(aCard); // add to deck
        }
        Collections.shuffle(theDeck); // prebaked shuffle method
    }
    
    /**
     * method which returns the current deck size
     * @return the deck size
     */
    private int getDeckSize() {
        return theDeck.size();
    }
    
    /**
     * method which draws a card from the deck if deck is not empty
     * otherwise, reinstantiate a new deck (I think casinos do this?)
     * @return the top card from the deck
     */
    private Card draw() {
        if (getDeckSize() == 0) {
            setUpDeck();
        }
        return theDeck.remove(0);
    }
    
    /**
     * method which takes a card from deck and puts into a hand
     * @param p - the player to draw to
     */
    void drawTo(Player p) {
        p.getHand().addCard(draw());
    }
    
    /**
     * method which initially populates a hand with 5 cards
     * @param p - the player to draw to
     */
    void drawInitial(Player p){ 
        for (int i = 0; i < 5; i++) {
            p.getHand().addCard(draw());
        }
    }
    
    /**
     * method which redraws cards previously discarded from a hand
     * @param i - position of card to discard from hand
     * @param p - the player to redraw a card to
     */
    void redraw(int i, Player p) {
        p.getHand().discardCard(i);
        drawTo(p);
    }
    
    /**
     * method to discard or "burn" top card of deck
     */
    void burn() {
        theDeck.remove(0);
    }
}