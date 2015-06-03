package de.eva.rmi.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.eva.rmi.chat.Command.ContentCommand;
import de.eva.rmi.chat.Command.ListCommand;
import de.eva.rmi.chat.Command.RegisterCommand;
import de.eva.rmi.chat.Command.ToSpecialUserCommand;

public interface ChatService extends Remote {
	void register(RegisterCommand c) throws RemoteException, UserAlreadyRegisteredException;
	void broadcast(ContentCommand m) throws RemoteException;
	void sendMessageToUser(ToSpecialUserCommand c) throws RemoteException;
	void listCommand(ListCommand l) throws RemoteException;
}
