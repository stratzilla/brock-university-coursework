package roborace.server;

public class Factory implements XMLObject {
	
    public void effect(EventList events, int phase, int playerID, Board board) {
        // denote if a decoration effect occurred or not
        boolean wasDecoration = false;
        // create a temporary event list
        EventList tempEventList = new EventList();
        // get robot of player
        Robot playerRobot = board.getRobotByID(playerID);
        // get position of that robot
        Point robotPosition = playerRobot.getPosition();
        // if there is a decoration at that position
        if (this.getDecorationAt(robotPosition) != null) {
            // do the decoration effect
            this.getDecorationAt(robotPosition).effect(tempEventList, 
                phase, playerID, board);
            // if decoration, there was a decoration
            wasDecoration = (!tempEventList.isEmpty());
        }
        // if there was not decoration, call effect of tile
        if (tempEventList.isEmpty()) {
            // get position of robot
            int x = robotPosition.x;
            int y = robotPosition.y;
            // effect on tile robot was on
            this.tiles[x][y].effect(tempEventList, playerID, board);
        } else { // else there was a decoration
            // add event start
            events.add(new EventEffectStart(robotPosition, 
                playerID, wasDecoration));
            // add effect to events
            events.addAll(tempEventList);
            // add event end
            events.add(new EventEffectEnd());            
        }
        /**
         * in the else above, I wasn't sure if this is meant to be another if
         * or not. i.e. does tiles.effect change events? I'm assuming not, so
         * an else would be appropriate.
         */
    }

}