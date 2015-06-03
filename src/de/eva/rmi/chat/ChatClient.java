package de.eva.rmi.chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import de.eva.rmi.chat.Command.ContentCommand;
import de.eva.rmi.chat.Command.ListCommand;
import de.eva.rmi.chat.Command.RegisterCommand;
import de.eva.rmi.chat.Command.ToSpecialUserCommand;

public class ChatClient extends UnicastRemoteObject implements ChatClientService {

	private static final long serialVersionUID = 954610397320847213L;

	protected ChatClient() throws RemoteException {
		super();
	}

	@Override
	public void receive(Command c) {
		if (c instanceof ContentCommand) {
			System.out.println(((ContentCommand) c).getContent());
		} else {
			System.out.println("got command " + c);
		}
	}

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, UserAlreadyRegisteredException {
		System.out.println("Try to connect to server...");
		ChatService service = (ChatService) Naming.lookup(ChatServer.CHAT_SERVER);
		
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
