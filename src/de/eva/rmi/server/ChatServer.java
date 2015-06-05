package de.eva.rmi.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import de.eva.rmi.chat.Command;
import de.eva.rmi.chat.Message;
import de.eva.rmi.chat.UserAlreadyRegisteredException;
import de.eva.rmi.chat.Command.ContentCommand;
import de.eva.rmi.chat.Command.ListCommand;
import de.eva.rmi.chat.Command.RegisterCommand;
import de.eva.rmi.chat.Command.ToSpecialUserCommand;
import de.eva.rmi.client.ChatClientService;

public class ChatServer extends UnicastRemoteObject implements ChatServerService {

	public static final String CHAT_SERVER = "chat-server";

	private Map<String, ChatClientService> userToClientMapping = new HashMap<>();
	private Map<ChatClientService, String> clientToUserNameMapping = new HashMap<>();
	
	private ChatServer() throws RemoteException {
		super();
	}

	@Override
	public synchronized void sendMessageToUser(ToSpecialUserCommand c) throws RemoteException { 
		ChatClientService receiver = userToClientMapping.get(c.getTo());
		if(receiver == null){
			System.err.println("no receiver found");
		} else {
			String userName = clientToUserNameMapping.get(c.getClient());
			receiver.receive(new Message(userName, c.getContent()));
		}
	}

	@Override
	public synchronized void listCommand(ListCommand l) throws RemoteException {
		String listedClientNames = userToClientMapping.keySet().toString();
		l.getClient().receive(new Message("server", listedClientNames));
	}

	@Override
	public synchronized void register(RegisterCommand c) throws RemoteException, UserAlreadyRegisteredException {
		String name = c.getContent();
		if(userToClientMapping.containsKey(name)){
			throw new UserAlreadyRegisteredException(name);
		}
		userToClientMapping.put(name, c.getClient());
		clientToUserNameMapping.put(c.getClient(), name);
	}

	@Override
	public synchronized void broadcast(ContentCommand m) throws RemoteException {
		for(ChatClientService c : userToClientMapping.values()){
			c.receive(new Message("bc" + clientToUserNameMapping.get(m.getClient()), m.getContent()));
		}
	}
	
	
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		Naming.rebind(CHAT_SERVER, new ChatServer());
		System.out.println("The server is running...");
	}


}
