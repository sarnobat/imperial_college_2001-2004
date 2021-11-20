/*************************************************************
UDPServer.java

Waits for a message and responds with a string that combines
parameters from the message body.

*************************************************************/

import java.net.DatagramPacket;
import java.lang.reflect.Array;

public class UDPServer extends UDPBase implements MessageHandlerI {

	public static void main(String args[]) {
		int recvPort, sendPort;

		// Get the parameters
		if (Array.getLength(args) < 1) {
			System.err.println("Arguments required: send port, recv port");
			System.exit(-1);
		}
		sendPort = Integer.parseInt(args[0]);
		recvPort = Integer.parseInt(args[1]);

		new UDPServer(sendPort, recvPort);
	}

	public UDPServer(int sendPort, int recvPort) {
		super(sendPort, recvPort);
		System.out.println("UDPServer ready");
	}

	public synchronized void receiveMsg(DatagramPacket pac) {
		MessageInfo msg;
		String reply;

		// Parse the message
		msg = new MessageInfo(pac);

		// Build our reply & send it back
		// Assume their recvPort == our recvPort
		send(getResponse(msg.msgStr, msg.msgInt), msg.msgInt, msg.senderAddress, recvPort);
	}

	public String getResponse(String message, int count) {
		return "You've said " + message + " " + count + " times?!";
	}
}
