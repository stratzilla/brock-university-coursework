package Poker;

/**
 * HandType determined by value enumeration for Five-Card Poker
 * @author Robert Scott - 2018
 */

    public enum HandType {
        ROYAL_FLUSH(10),
        STRAIGHT_FLUSH(9),
        FOUR_OF_A_KIND(8),
        FULL_HOUSE(7),
        FLUSH(6),
        STRAIGHT(5),
        THREE_OF_A_KIND(4),
        TWO_PAIR(3),
        PAIR(2),
        HIGH_CARD(1);
        
        private final int value;
        
        private HandType(final int v) {
            value = v;
        }
        
        public int getVal() {
            return value;
        }
    }
