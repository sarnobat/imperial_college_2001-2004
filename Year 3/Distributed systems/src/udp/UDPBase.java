/*************************************************************
UDPBase.java

Provides basic UDP packet send/recieve functionality, must
be extended by any class that wishes to use it's features.

TODO:
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

		try {
			/*DONE: Open sockets on the ports to send and recv from **/
			sendSoc = new DatagramSocket(sendPort);
			//System.out.println("About to create a receive socket with port: " + recvPort);
			recvSoc = new DatagramSocket(recvPort);
		}
		catch (SocketException e) {
			System.out.println("Could not initialize sockets. " +e);
			System.exit(-1); 
		}		

		// rec listens to the port for packets and delivers them to receiveMsg
		//System.out.println("About to create a port listener for port: " + recvSoc.getLocalPort());
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

		/** DONE: Construct datagram and send it **/
		try {
			byte[] temp = payload.getBytes();
			DatagramPacket pkt = new DatagramPacket(temp,temp.length,destAddr,destPort);
			sendSoc.send(pkt);
		}
		catch (IOException e) {
			System.out.println("Could not send datagram. " +e);
			System.exit(-1);
		}

	}

	public abstract void receiveMsg(DatagramPacket pac);

	public synchronized void close() {
		rec.close();
	}
}
