public void execute(AnimatedBoard board, InfoPane infoPane) {
	board.getRobotByID(this.playerID).step(this.direction);
}