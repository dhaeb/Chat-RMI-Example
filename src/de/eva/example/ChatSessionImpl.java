
package de.eva.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;


public class ChatSessionImpl extends UnicastRemoteObject implements ChatSession {
  ChatServerImpl server;
  String nickname;
  ClientHandle handle;

  public ChatSessionImpl() throws RemoteException {
  }

  public ChatSessionImpl(ChatServerImpl server, String nickname, ClientHandle handle) throws RemoteException {
    this.server = server;
    this.nickname = nickname;
    this.handle = handle;
  }

  public void sendMessage(String message) {
    server.postMessage(message, this);
  }

  public ClientHandle getClientHandle() {
    return handle;
  }

  public String getNickname() {
    return nickname;
  }

}