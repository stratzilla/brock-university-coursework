public void execute(EventList events, Board board) {
	// get ID of player holding this card
	int playerID = this.getID();
	// get robot ID of corresponding player
	Robot executingPlayer = board.getRobotByID(playerID);
	if (executingPlayer.isAlive()) { // if the player's robot is alive
		// denote start of event
		events.add(new EventCardStart(this));
		// iteratively move as long as robot is alive
		for (int i = 0; i < steps && executingPlayer.isAlive(); i++) {
			// executing step in same direction
			board.step(events, playerID, executingPlayer.getDirection());   
		}
		// denote end of event
		events.add(new EventCardEnd());
	}
}