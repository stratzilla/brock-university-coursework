// set up server socket
ServerSocket serverSocket = null;
try {
	serverSocket = new ServerSocket(PORT);
} catch (IOException ex) {
	Logger.getLogger(RoboRaceServer.class.getName()).log(Level.SEVERE, null, ex);
}
String[] playerNames = new String[nHuman]; // player names
NetworkPort[] playerPorts = new NetworkPort[nHuman]; // player ports
for (int i = 0; i < nHuman; i++) { 
	try {
		// for each player
		// instantiate new network port based on socket
		playerPorts[i] = new NetworkPort(serverSocket.accept());
	} catch (IOException ex) {
		Logger.getLogger(RoboRaceServer.class.getName()).log(Level.SEVERE, null, ex);
	}
	// get player name from socket
	playerNames[i] = playerPorts[i].receive();
	playerPorts[i].send(Integer.toString(i));
}
(new GameMaster(playerNames, playerPorts)).run();