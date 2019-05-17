package FivePoker;

/**
 * Main driver for Five-Card Poker
 * @author Robert Scott - 2018
 * 
 */
public class main {
    
    /**
     * main driver for program; everything is done in Board class
     * which invokes the other class methods. Main driver is just to initialize
     * the game.
     */
    public main() {
        Board theBoard = new Board();
    }

    public static void main(String[] args) {
        main m = new main();
    }  
}