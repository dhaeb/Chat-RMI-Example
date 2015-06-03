package de.eva.example;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {
	List sessions = new ArrayList();

	public ChatServerImpl() throws RemoteException {
	}

	public ChatSession createSession(String nickname, ClientHandle handle)
			throws RemoteException {
		System.out.println("create session: " + nickname);
		ChatSession s = new ChatSessionImpl(this, nickname, handle);
		sessions.add(s);
		return s;
	}

	public void postMessage(String message, ChatSessionImpl s) {
		ChatSessionImpl tmp;
		for (int i = 0; i < sessions.size(); i++) {
			tmp = (ChatSessionImpl) sessions.get(i);
			try {
				tmp.getClientHandle().receiveMessage(s.getNickname(), message);
			} catch (RemoteException ex) {
				System.out.println("unabled to contact client "
						+ s.getNickname());
				System.out.println("removing.");
				removeSession(tmp);
				i--; // Da nun alle Clients in Liste einen Platz nach unten
						// rutschen ...
			}
		}
	}

	public void removeSession(ChatSession session) {
		sessions.remove(session);
	}

	public static void main(String args[]) {
		try {
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			Naming.rebind("chat-server", new ChatServerImpl());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}