public void execute(AnimatedBoard board, InfoPane infoPane) {
	String playerName = board.getRobotByID(this.card.getID()).getName();
	infoPane.displayCardInfo(this.card.getImage(), playerName);
}