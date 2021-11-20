/*************************************************************
RMIServerI.java

Remote inte> ace for the RMI Server class

*************************************************************/

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerI
	extends Remote
 	{
	public String getResponse(String message, int count) throws RemoteException;
}
