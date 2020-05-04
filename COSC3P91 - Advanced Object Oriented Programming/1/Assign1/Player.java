package FivePoker;

/**
 * Player class for Five-Card Poker
 */
public final class Player {
    
    private final String name; // name of player
    private Hand playerHand; // the hand a player owns
    private final double money; // how much money they possess
    
    /**
     * constructor for Player class
     * @param n - the name of the player
     */
    Player(String n) {
        name = n;
        money = 500.00; // let's say initial money is $500
        makeHand(); // initial setup of hand
    }
    
    /**
     * method which instantiates empty hand
     */
    private void makeHand() {
        playerHand = new Hand();
    }
    
    /**
     * method to get the hand a player has
     * @return the player's hand
     */
    protected Hand getHand() {
        return playerHand;
    }
    
    /**
     * method to get the score of a player's hand
     * @return the hand score
     */
    int getScore() {
        return getHand().getScore();
    }
    
    /**
     * method to get the name of the player
     * @return the name of the player
     */
    String getName() {
        return name;
    }
    
    /**
     * method to print the name of the player
     */
    void printName() {
        System.out.println(getName());
    }    
}
