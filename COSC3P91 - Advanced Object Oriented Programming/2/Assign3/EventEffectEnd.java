public void execute(AnimatedBoard board, InfoPane infoPane) {
	board.waitOnAnimations();
	try {
		Thread.sleep(1000);
	} catch (InterruptedException ex) {
		Logger.getLogger(EventCardEnd.class.getName()).log(Level.SEVERE, null, ex);
	}
	infoPane.waitOnSingleStep();
	infoPane.displayNoInfo();
}