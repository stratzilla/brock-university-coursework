package roborace.server;

public class TileGoal implements Tile {
	
    @Override
    public void effect(EventList events, int playerID, Board board) {
        // denote winning player
        events.add(new EventVictory(playerID));
    }

}