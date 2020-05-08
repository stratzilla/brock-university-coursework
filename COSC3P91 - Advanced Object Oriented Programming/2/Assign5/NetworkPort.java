private BufferedReader inStream;
private PrintWriter outStream;

public NetworkPort(Socket socket) {
	try {
		// attempt to instantiate a reader and writer
		InputStreamReader inReader = 
			new InputStreamReader(socket.getInputStream());
		// had to add an import here; this is from slides
		OutputStreamWriter outWriter =
			new OutputStreamWriter(socket.getOutputStream());
		inStream = new BufferedReader(inReader);
		outStream = new PrintWriter(outWriter);
	} catch (IOException ex) {
		Logger.getLogger(NetworkPort.class.getName()).log(Level.SEVERE, null, ex);
	}
}

public synchronized void send(String message) { 
	// output message
	outStream.println(message);
	// output termination character
	outStream.println('\0');
	// flush the buffer
	outStream.flush();
}

public synchronized String receive()  {
	// string to finally return
	String toReturn = "";
	// denote when to terminate while loop below
	boolean termination = false;
	while(!termination) { // while termination condition not met
		String line = "";
		try {
			// read the line
			line = inStream.readLine();
		} catch (IOException ex) {
			// if error, denote break condition
			termination = true;
			Logger.getLogger(NetworkPort.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (line.contains("\0")) {
			// as well, break condition if line includes termination char
			termination = true;
		} else {
			// otherwise append it to returning message
			toReturn += line;
		}
	}
	return toReturn;
}