/*************************************************************
MessageInfo.java

This class holds the UDP message information and parses it as
necessary.

*************************************************************/

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.StringTokenizer;

public class MessageInfo {

	public	InetAddress	senderAddress;
	public 	String 	msgStr;
	public 	int		msgInt;

	public MessageInfo(DatagramPacket pac) {
		String			dataString;
		StringTokenizer	toker;
		String			token;

		// Parse the message. This makes some assumptions, including:
		// 1) that the message format is iiii;sssss
		// 2) that the message is contained in one datagram
		dataString = new String(pac.getData());
		toker = new StringTokenizer(dataString, ";");
		if (toker.hasMoreTokens()) {

				// First Token is the Integer
				token = toker.nextToken();
				msgInt = (Integer.valueOf(token)).intValue();

				// Next token is the String
				token = toker.nextToken();
				msgStr = token;

				token = toker.nextToken();
		}

		// record source address
		senderAddress = pac.getAddress();
	}
}
