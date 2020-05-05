package roborace.server;

public class DecorationCrusher extends Decoration {
	
    @Override
    public void effect(EventList events, int phase, int playerID, Board board) {
        // get robot from playerID
        Robot playerRobot = board.getRobotByID(playerID);
        // determine if phase is one of phase1-2
        boolean phaseTruth = (phase == phase1)
                          || (phase == phase2);
        // if robot alive and phase is any of phase1-2
        if (playerRobot.isAlive() && phaseTruth) {
            // destroy robot
            playerRobot.destroyed();
            // add destroy evvent to events list
            events.add(new EventDestroyed(playerID));
        }
    }

}