package de.eva.rmi.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClientService extends Remote {

	void receive(Command c) throws RemoteException;
}
