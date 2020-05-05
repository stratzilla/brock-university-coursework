package roborace.server;

public class TileBelt implements Tile {
	
    @Override
    public void effect(EventList events, int playerID, Board board) {
        // move robot in indicated direction
        board.step(events, playerID, direction);
    }

}