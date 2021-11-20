/*************************************************************
RMIServer.java

Implements the getResponse method that builds a message from
the parameters passed and returns it as a string.

TODO:
Modify this class so that it is a valid RMI server class
and add the code that will bind it to a RMI Registry

Your code should ensure that it is *not* necessary to start
an RMI registry manually before starting this server.

*************************************************************/

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	public static void main(String args[]) {
		String serverURL = null;
		RMIServer server = null;

		// Define the URL we'll offer ourselves as
		try {
			serverURL = "rmi://" + InetAddress.getLocalHost().getHostName() + "/RMIServer";
		}
		catch (UnknownHostException e) {
			System.err.println("RMIServer start-up failed (defining URL), unknown host exception: " + e);
			System.exit(-1);
		}

		/** DONE: Setup Security Manager **/
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		try {
			Runtime r = Runtime.getRuntime();
			r.exec("rmiregistry");
		}
		catch (IOException e2) {
			System.out.println("Couldn't start registry." + e2);
			System.exit(-1);
		}

		/** DONE: Create Server Object and bind to registry **/
		try {
			server = new RMIServer();
			Naming.rebind(serverURL, server);
		}
		catch (Exception e1) {
			System.out.println("Could not register Server: " + e1);
			System.exit(-1);
		}

		System.out.println("RMIServer ready at " + serverURL);
	}

	public RMIServer() throws RemoteException // this is a result of extending UnicastRemoteObject
	{

	}

	/** DONE: Implementation of the getResponse(..) method **/
	public String getResponse(String message, int count) throws RemoteException {
		return ("You've said " + message + " " + count + " times.");
	}

}
