public void execute(AnimatedBoard board, InfoPane infoPane) {
	infoPane.displayRunDecorationsInfo();
	board.runDecorations(this.phase);
	board.waitOnAnimations();
	infoPane.waitOnSingleStep();
	infoPane.displayNoInfo();
	infoPane.increasePhase();
}
