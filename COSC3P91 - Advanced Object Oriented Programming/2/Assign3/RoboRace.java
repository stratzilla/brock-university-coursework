public static void main(String[] args) {
	String[] names = new String[nHuman];
	// make an array of plays
	Player[] players = new Player[nHuman];
	// game master ports
	Port[] gameMasterPorts = new Port[nHuman];
	for (int i = 0; i < nHuman; i++) {
		names[i] = GameDialogs.showInputDialog("Player #" + (i + 1), "Name of Player #" + (i + 1) + ":",null);
		// make a channel per each player
		Channel playerChannel = new Channel();
		// does it matter which port? It looks like as long as its
		// consistent, it can be either Port1 or Port2
		players[i] = new Player(i, playerChannel.asPort1());
		// assign other port to GM
		gameMasterPorts[i] = playerChannel.asPort2();
	}
	for (Player p : players) {
		// start each player thread
		Thread newThread = new Thread(p);
		newThread.start();
	}
	// run game master
	(new GameMaster(names, gameMasterPorts)).run();
}	   
