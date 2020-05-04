package Poker;

import Cards.Deck;
import Cards.StandardDeck;

/**
 * Dealer class for Five-Card Poker
 */
public class Dealer {
    
    public Deck theDeck; // for this case, typical 52-card variant
    
    /**
     * constructor for Dealer class
     */
    public Dealer() {
        theDeck = new StandardDeck(); // init deck for dealer to use
    }
    
    /**
     * method to discard top card of deck (burn card)
     */
    public void burnCard() {
        theDeck.draw(); // although method returns Card, don't need it
    }
    
    /**
     * method to deal a hand to a given player
     * @param p - the player to deal to
     */
    public void dealHand(PokerPlayer p) {
        for (int i = 0; i < 5; i++) { // hand has 5 cards
            p.getHand().addCard(theDeck.draw());
        }
    }
    
    /**
     * method to redraw cards to a player's hand after discarding
     * @param p - the player to work with
     * @param ba - boolean array to determine which of the hand to redraw
     */
    public void redrawCard(PokerPlayer p, boolean[] ba) {
        int i = 0;
        /**
         * it was interesting in the provided UML to see a bool array used
         * here. I wouldn't have done it personally but it makes sense
         */
        for (boolean b : ba) {
            if (b) { // if true, work on that card
                p.getHand().discardCard(i); // discard that card
                p.getHand().addCard(theDeck.draw()); // then replace with new
            }
            i++;
        }
    }
}
