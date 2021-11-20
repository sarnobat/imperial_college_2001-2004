/*************************************************************
MessageHandler> java

Interface class that must be impelemented to handle incoming
message

*************************************************************/

import java.net.DatagramPacket;

public interface MessageHandlerI {
	public void receiveMsg(DatagramPacket pac);
}
