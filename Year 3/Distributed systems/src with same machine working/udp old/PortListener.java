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
		//presumably, the hander is either the server or the client
		this.handler = handler;
		this.soc = soc;
		close = false;
	}

	public void run() {
		// a separate thread to listen for incoming datagrams
		// this allows the client to handle sending and receiving asynchronously & without time-out on loss

		while (!close) {
			
			/** TODO: Receive data and return it using callback method of handler **/
			//TODO: Not sure what the callback method is; Not sure what the point of the handler is.
			byte[] buffer;
			DatagramPacket request,reply = null;
			try {
				buffer = new byte[1000];
				request = new DatagramPacket(buffer,buffer.length);
				soc.receive(request);
				reply = new DatagramPacket(request.getData(),soc.getReceiveBufferSize(),request.getAddress(),request.getPort());
				handler.receiveMsg(reply);
			}
			catch (IOException e) {
				System.out.println("Error receiving datagram packet. " + e);
				//TODO: System.exit is unacceptable. You should not close the application
				// when something goes wrong
				System.exit(-1);
			}
		}
	}

	public void close() {
		close = true;
		soc.close();
	}
}
