public void run() {
	// this is taken almost literal from the slides
	
	// create double buffer strategy
	this.createBufferStrategy(2);
	// get bufferstrategy from this frame
	BufferStrategy strategy = this.getBufferStrategy(); 
	// keep track of previous loop time, current time, and difference
	long lastLoopTime = System.currentTimeMillis();
	long currentTime = 0;
	long delta = 0;
	// to store graphics while running
	Graphics graphics;
	while (running) { // while game is running
		// get current time
		currentTime = System.currentTimeMillis();
		// find delta as function of current and previous
		delta = currentTime - lastLoopTime;
		// update previous time
		lastLoopTime = currentTime;
		// update animations
		this.board.update(delta);
		// get graphics
		graphics = strategy.getDrawGraphics();
		// draw the graphics
		this.board.draw(graphics);
		// dispose of current graphics frame
		graphics.dispose();
		// show double buffer
		strategy.show();
		// to prevent negative delta
		if (delta < 10) {
			try { 
				// sleep on thread
				Thread.sleep(10-delta); 
			} catch (InterruptedException e) {}
		}
	}
}	