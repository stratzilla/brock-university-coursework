package roborace.server;

public class TileGear implements Tile {
	
    @Override
    public void effect(EventList events, int playerID, Board board) {
        // rotate robot 90 deg
        board.getRobotByID(playerID).turn90(clockwise);
        // add event to eventlist
        events.add(new EventTurn90(playerID, clockwise));
    }

}