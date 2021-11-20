/*************************************************************
RMIClient.java

Binds to server and calls getResponse method for as many
iterations as specified in command line parameters.  Times
how long the entire process takes.

TO-DO:
Bind to your server and make the calls to getResponse in
the manner specified in Question 1b.

*************************************************************/

import java.rmi.*;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Date;

public class RMIClient {

	public static void main(String args[]) {
		//args = { server, message count, message string } 
		String serverURL;
		int countTo = Integer.parseInt(args[1]);
		String message = args[2];

		// Get the parameters
		if (Array.getLength(args) < 3) {
			System.err.println("Arguments required: server IP/name, message count, message");
			System.exit(-1);
		}
		serverURL = new String("rmi://" + args[0] + "/RMIServer");

		/** DONE: Set up a security manager **/
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		new RMIClient(serverURL, countTo, message);
	}

	public RMIClient(String serverURL, int countTo, String message) {
		RMIServerI server = null;
		int answers, tries;
		Date fromTime, toTime;
		String response;

		try {
			server = (RMIServerI) Naming.lookup(serverURL);

		}
		catch (Exception e) {
			System.out.println("Could not bind to Server");
			e.printStackTrace();
		}

		// set up house keeping
		answers = 0;
		tries = 0;
		response = null;
		fromTime = new Date();

		while(answers<countTo){
			response = null;
			try {
				/** DONE: Send/Receive messages according to problem specification **/
				response = server.getResponse(message, countTo);
			}
			catch (Exception e1) {
				System.out.println("Couldn't get response from server");
			}
			if(response != null) {
				answers++;
			}
			tries++;
		}

		toTime = new Date();

		// Report the time
		System.out.println(answers + " answers from " + tries + " RMI call attempts");
		System.out.println("Last message was: " + response);
		System.out.println("Took " + (toTime.getTime() - fromTime.getTime()) + "ms in the loop (beware - not CPU time used!)");

		System.out.println();
		String localMachine = null;
		try {
			localMachine = InetAddress.getLocalHost().getHostName();
		}
		catch (Exception e) {
			System.out.println("Coudln't determine local machine name");
		}
		System.out.println(
			"Therefore, average time to invoke remote object at "
				+ serverURL
				+ " from client "
				+ localMachine
				+ " = "
				 + (toTime.getTime() - fromTime.getTime())/(double)answers + "ms.");

	}
}
