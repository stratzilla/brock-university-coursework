package FivePoker;

/**
 * Tiebreaker class for Five-Card Poker
 * @author Robert Scott - 2018
 */
public class Tiebreaker {
    
    /**
     * empty constructor (may use later?)
     */
    public Tiebreaker() {}
    
    /**
     * method which finds the higher scorer between
     * two players. Is used repeatedly in the case of
     * more than two players to determine a winner
     * in board class
     * @param p1 - first player to compare
     * @param p2 - second player to compare
     * @return the winner
     */
    Player findWinner(Player p1, Player p2) {
        /**
         * this whole method is only useful when a tie exists
         * so make sure there's a tie first. Otherwise the winner
         * is clear
         */
        if (p1.getScore() > p2.getScore()) { return p1; }
        if (p2.getScore() > p1.getScore()) { return p2; }
        /**
         * if you've reached this far, a tie between p1 and p2 exists
         * and you need to iterate through hands to determine who has the
         * higher score
         */
        for (int i = 0; i < 5; i++) {
            // man this is ugly
            int p1Value = p1.getHand().getHand().get(i).getRank();
            int p2Value = p2.getHand().getHand().get(i).getRank();
            if (p1Value > p2Value) { return p1; }
            if (p2Value > p1Value) { return p2; }
        }
        // if for some reason you get here, assume p1 is winner
        return p1;
    }
}
