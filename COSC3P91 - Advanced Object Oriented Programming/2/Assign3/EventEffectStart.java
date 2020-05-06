public void execute(AnimatedBoard board, InfoPane infoPane) {
	String playerName = board.getRobotByID(this.playerID).getName();
	infoPane.displayEffectInfo(playerName);
	if (this.isDecorationEffect) {
		board.runDecorationAt(new Point(x, y));
	}
}