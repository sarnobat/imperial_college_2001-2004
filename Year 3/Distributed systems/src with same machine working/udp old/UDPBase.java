/*************************************************************
UDPBase.java

Provides basic UDP packet send/recieve functionality, must
be extended by any class that wishes to use it's features.

TO-DO:
Open sockets on the appropriate ports for sending/receiving
Construct the datagram and send it

*************************************************************/

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.InetAddress;
import java.io.IOException;

public abstract class UDPBase implements MessageHandlerI {

	protected int sendPort, recvPort;
	protected DatagramSocket sendSoc, recvSoc;
	protected PortListener rec;

	public UDPBase(int sendPort, int recvPort) {
		String message, response;
		Thread runner;

		this.sendPort = sendPort;
		this.recvPort = recvPort;

		/** Open sockets on the ports to send and recv from **/
		try {			
			sendSoc = new DatagramSocket(sendPort);
			recvSoc = new DatagramSocket(recvPort);
		}
		catch (SocketException e) {
			System.out.println("Couldn't bind to port: " + e);
			System.exit(-1);
		}
		

		// rec listens to the port for packets and delivers them to receiveMsg
		rec = new PortListener(this, recvSoc);
		runner = new Thread((Runnable) rec);
		runner.start();
	}

	protected void send(String msgStr, int msgInt, InetAddress destAddr, int destPort) {
		String message;

		message = new String(msgInt + ";" + msgStr + ";");
		sendDatagram(message, destAddr, destPort);
	}

	protected synchronized void sendDatagram(String payload, InetAddress destAddr, int destPort) {
		
		/** Construct datagram and send it **/
		DatagramSocket soc = null;
		DatagramPacket pkt = new DatagramPacket(payload.getBytes(),payload.getBytes().length,destAddr,destPort);
		try {
			soc = new DatagramSocket();
			
		}
		catch (SocketException e) {
			System.out.println("Could not initialize socket. "+e);
			System.exit(-1);
		}
		try {
			soc.send(pkt);
		}
		catch (IOException e1) {
			System.out.println("Could not send packet. " + e1);
			System.exit(-1);
		}

	}

	public abstract void receiveMsg(DatagramPacket pac);

	public synchronized void close() {
		rec.close();
	}
}
