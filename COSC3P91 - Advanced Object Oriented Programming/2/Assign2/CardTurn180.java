public void execute(EventList events, Board board) {
	// get ID of player holding this card
	int playerID = this.getID();
	// get robot ID of corresponding player
	Robot executingPlayer = board.getRobotByID(playerID);
	if (executingPlayer.isAlive()) { // if the player's robot is alive
		// denote start of event
		events.add(new EventCardStart(this));
		// rotate robot 180deg
		executingPlayer.turn180();
		events.add(new EventTurn180(playerID));
		// denote end of event
		events.add(new EventCardEnd());
	}
}