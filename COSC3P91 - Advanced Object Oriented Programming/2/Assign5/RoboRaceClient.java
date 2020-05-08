NetworkPort playerPort; // port for player
int playerID; // player's identification
try {
	// get port from serversocket
	playerPort = new NetworkPort(new Socket(HOST, PORT));
	// send player name to server
	playerPort.send(name);
	// get player id from server
	playerID = Integer.parseInt(playerPort.receive());
	// instantiate new player and run
	(new Player(playerID, playerPort)).run();
} catch (IOException ex) {
	Logger.getLogger(RoboRaceClient.class.getName()).log(Level.SEVERE, null, ex);
}