/*************************************************************
UDPClient.java

Uses UDPBase features to send messages to the server.
Performs timing operations to determine to elapsed time to
send/receive the messages.

*************************************************************/

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.ReadableByteChannel;
import java.lang.reflect.Array;
import java.util.Date;

public class UDPClient extends UDPBase implements MessageHandlerI {

	protected String lastMsgRecvd;
	protected int countTo, answersRecvd, msgsLost;

	public static void main(String args[]) {
		InetAddress serverAddr = null;
		int recvPort, sendPort;
		int countTo;
		String message;

		// Get the parameters
		if (Array.getLength(args) < 4) {
			System.err.println("Arguments required: server name/IP, send port, recv port, message count, message");
			System.exit(-1);
		}

		try {
			serverAddr = InetAddress.getByName(args[0]);
		}
		catch (UnknownHostException e) {
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		sendPort = Integer.parseInt(args[1]);
		recvPort = Integer.parseInt(args[2]);
		countTo = Integer.parseInt(args[3]);
		message = args[4];

		// during function test - to stop this blasting the network
		//if (countTo > 10)
		//	System.exit(-2);

		new UDPClient(serverAddr, sendPort, recvPort, countTo, message);
	}

	public UDPClient(InetAddress serverAddr, int sendPort, int recvPort, int countTo, String message) {
		super(sendPort, recvPort);
		lastMsgRecvd = null;
		this.countTo = countTo;
		this.msgsLost = 0;
		testLoop(serverAddr, message);
		close();
	}

	protected void testLoop(InetAddress serverAddr, String message) {
		//pre: message must be of the form: iii;ssss
		int tries;
		Date fromTime, toTime;
		DatagramPacket pkt;
				
		// set up house keeping
		answersRecvd = 0;
		tries = 0;
		fromTime = new Date();

		/** TODO: Send messages according to problem specification **/
		
		while(answersRecvd<countTo){
			//TODO: Not sure if 3rd argument is okay
			sendDatagram(message,serverAddr,3000);
			/*DatagramPacket receivedPacket = new DatagramPacket(new byte[100],100);
			receiveMsg(receivedPacket);*/
			tries++;
		}
		
		toTime = new Date();

		// Report the time
		System.out.println(answersRecvd + " answers from " + tries + " UDP messages");
		System.out.println("Lost " + msgsLost + " UDP messages");
		System.out.println("Last message was: " + lastMsgRecvd);
		System.out.println("Took " + (toTime.getTime() - fromTime.getTime()) + "ms in the loop");
	}

	public synchronized void receiveMsg(DatagramPacket pac) {
		MessageInfo msg;
		String reply;

		/** TODO: Handle the received message **/
		//DatagramPacket receivedPacket = new DatagramPacket(new byte[100],100);
		

	}
}
