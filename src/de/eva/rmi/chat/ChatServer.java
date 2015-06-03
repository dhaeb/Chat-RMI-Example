package de.eva.rmi.chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import de.eva.rmi.chat.Command.ContentCommand;
import de.eva.rmi.chat.Command.ListCommand;
import de.eva.rmi.chat.Command.RegisterCommand;
import de.eva.rmi.chat.Command.ToSpecialUserCommand;

public class ChatServer extends UnicastRemoteObject implements ChatService {

	public static final String CHAT_SERVER = "chat-server";

	private static final long serialVersionUID = 1323539002891588806L;

	private Map<String, ChatClientService> userToClientMapping = new HashMap<>();
	
	private ChatServer() throws RemoteException {
		super();
	}

	@Override
	public synchronized void sendMessageToUser(ToSpecialUserCommand c) throws RemoteException { 
		ChatClientService receiver = userToClientMapping.get(c.getTo());
		if(receiver == null){
			System.err.println("no receiver found");
		} else {
			receiver.receive(c);
		}
	}

	@Override
	public synchronized void listCommand(ListCommand l) throws RemoteException {
		ContentCommand sendable = new ContentCommand(l.getClient(), userToClientMapping.keySet().toString());
		l.getClient().receive(sendable);
	}

	@Override
	public synchronized void register(RegisterCommand c) throws RemoteException {
		userToClientMapping.put(c.getContent(), c.getClient());
	}

	@Override
	public synchronized void broadcast(ContentCommand m) throws RemoteException {
		for(ChatClientService c : userToClientMapping.values()){
			c.receive(m);
		}
	}
	
	
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		Naming.rebind(CHAT_SERVER, new ChatServer());
		System.out.println("The server is running...");
	}


}
