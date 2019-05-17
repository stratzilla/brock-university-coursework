package FivePoker;

/**
 * Card class for Five-Card Poker.
 * @author Robert Scott - 2018
 */
public class Card {
    
    private static final String[] SUITS = {"\u2660","\u2665","\u2666","\u2663"};
    private static final String[] RANKS = {"2","3","4","5","6","7","8","9","10"
                                            ,"J","Q","K","A"};
    private final int cardNum; // the number of a card (1-52)
    
    /**
     * constructor for card class
     * @param c - card number (1-52 for standard 52-deck) initialize to
     */
    Card(int c) {
        cardNum = c;
    }
    
    /**
     * method to get the rank of a card using modulo operators
     * @return the rank/face value of the card
     */
    int getRank() {
        return cardNum % 13; // assuming 52-card deck, 13 of each suit
    }
    
    /**
     * method to get the suit of a card using modulo operators
     * @return the suit of the card
     */
    int getSuit() {
        return cardNum % 4; // assuming 52-card deck, 4 of each rank
    }
    
    /**
     * method which finds the 'word' of a rank if applicable
     * for example, 1 becomes Ace, 13 becomes King
     * @return the rank as an English word
     */
    String getRankString() {
        return RANKS[getRank()];
    }
    
    /**
     * method which finds the 'word' of a suit
     * for example, 1 becomes heart
     * @return the suit as an English word
     */
    String getSuitString() {
        return SUITS[getSuit()];
    }
    
    /**
     * method which uses the above two to work a sentence describing a card
     * @return a string which describes the card
     */
    String getCard() {
        return getRankString() + getSuitString();
    }    
}