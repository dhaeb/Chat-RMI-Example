
package de.eva.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientHandle extends Remote {

  public void receiveMessage(String nickname, String message) throws RemoteException;

}