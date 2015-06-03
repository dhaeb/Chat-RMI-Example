
package de.eva.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {

  public ChatSession createSession(String nickname, ClientHandle handle)
                                        throws RemoteException;

}