package Poker;

import Cards.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Hand class for Five-Card Poker
 * @author Robert Scott - 2018
 */
public class Hand {
    
    private final ArrayList<Card> hand; // the hand itself
    private Score handValue; // the value of said hand
    
    /**
     * constructor for hand class
     */
    protected Hand() {
        hand = new ArrayList<>(); // init empty AL
    }
    
    /**
     * method to get the top rank of a hand (note hands are sorted)
     * @return the top rank
     */
    protected Card topRank() {
        return hand.get(0);
    }
    
    /**
     * method which adds a card to hand
     * @param c - the card being added
     */
    protected void addCard(Card c) {
        if (getSize() < 5) { // only if hand size isn't five
            hand.add(c);
        }
        if (getSize() == 5) { // a score can be found for hands of 5
            handValue = new Score(this); // find the score
        }
        if (getSize() > 5) {
            throw new IllegalArgumentException("A hand contains only five cards");
        }
        /**
         * when first getting a 5-card hand or when redrawing back
         * to a 5-card hand, it's important to order it by rank (ascending)
         */
        Collections.sort(hand, new CompareHand());   
    }
    
    /**
     * method which discards a card from your hand
     * @param i - the indexed card to discard (1-5)
     */
    protected void discardCard(int i) {
        hand.remove(i);
    }
    
    /**
     * method which prints your hand
     * @return 
     */
    public String showHand() {
        String handString = "";
        for(Card c : hand) {
            handString += c.getCard() + " ";
        }
        return handString;
    }
    
    /**
     * method which gets the score value of a hand
     * @return the score
     */
    public HandType getScore() {
        return handValue.getScore();
    }
    
    /**
     * method to access the hand
     * @return the hand
     */
    public ArrayList<Card> getHand() {
        return hand;
    }
    
    /**
     * method to find the size of a hand
     * @return the size
     */
    private int getSize() {
        return hand.size();
    }
    
    /**
     * comparator class for Card class
     * used for sorting hand
     */
    private class CompareHand implements Comparator<Card> {
        @Override
        public int compare(Card o1, Card o2) {
            return o2.getRank() - o1.getRank();
        }
    }
}
