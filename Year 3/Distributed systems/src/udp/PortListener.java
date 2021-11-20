/*************************************************************
PortListener.java

Generic listener that will performs a call back to the handler
object that created it with any messages received

TODO:
Listen for incoming data, extract it into Datagram and pass
it back to the handler

*************************************************************/

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.io.IOException;

public class PortListener implements Runnable {

	protected MessageHandlerI handler;
	protected DatagramSocket soc;
	protected boolean close;

	public PortListener(MessageHandlerI handler, DatagramSocket soc) {
		this.handler = handler;
		this.soc = soc;
		close = false;
	}

	public void run() {
		// a separate thread to listen for incoming datagrams
		// this allows the client to handle sending and receiving asynchronously & without time-out on loss

		while (!close) {

			/** DONE: Receive data and return it using callback method of handler **/
			DatagramPacket p;
			try {
				byte[] buf = new byte[soc.getReceiveBufferSize()];
				p = new DatagramPacket(buf,buf.length);
				soc.receive(p);
				handler.receiveMsg(p);
			}
			catch (SocketException e) {
				System.out.println("Could not determine buffer size");
				System.exit(-1);
			}
			catch (IOException e1) {
				System.out.println("Could not send reply");
				System.exit(-1);
			}

		}
	}

	public void close() {
		close = true;
		soc.close();
	}
}
