package de.eva.rmi.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import de.eva.rmi.chat.Command;
import de.eva.rmi.chat.Message;
import de.eva.rmi.chat.UserAlreadyRegisteredException;
import de.eva.rmi.chat.Command.ContentCommand;
import de.eva.rmi.chat.Command.ListCommand;
import de.eva.rmi.chat.Command.RegisterCommand;
import de.eva.rmi.chat.Command.ToSpecialUserCommand;
import de.eva.rmi.server.ChatServer;
import de.eva.rmi.server.ChatServerService;

public class ChatClient extends UnicastRemoteObject implements ChatClientService {

	private static final long serialVersionUID = 954610397320847213L;

	protected ChatClient() throws RemoteException {
		super();
	}

	@Override
	public synchronized void receive(Message m) {
		System.out.println(String.format("[%s] %s", m.getFrom(), m.getContent()));
	}

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, UserAlreadyRegisteredException {
		System.out.println("Try to connect to server...");
		ChatServerService service = (ChatServerService) Naming.lookup(ChatServer.CHAT_SERVER);
		
		System.out.println("Please insert your user name...");
		ChatClient self = new ChatClient();
		try (Scanner s = new Scanner(System.in)) {
			s.useDelimiter("\n");
			String name = s.next();
			service.register(new RegisterCommand(self, name));
			System.out.println("Successfully registered as " + name);
			while (true) {
				String currentMessage = s.next();
				switch (currentMessage) {
				case "@LIST":
					service.listCommand(new ListCommand(self));
					break;
				default:
					if (currentMessage.startsWith("@")) {
						int firstWhitespaceIndex = currentMessage.indexOf(" ");
						String content = currentMessage.substring(firstWhitespaceIndex + 1);
						String to = currentMessage.substring(1,firstWhitespaceIndex);
						service.sendMessageToUser(new ToSpecialUserCommand(self, content, to));
					} else {
						service.broadcast(new ContentCommand(self, currentMessage));
					}
				}
			}
		}
	}

}
