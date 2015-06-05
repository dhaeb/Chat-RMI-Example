package de.eva.rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.eva.rmi.chat.Message;

public interface ChatClientService extends Remote {

	void receive(Message m) throws RemoteException;
}
