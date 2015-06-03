package de.eva.rmi.chat;

public class UserAlreadyRegisteredException extends Exception {

	private static final long serialVersionUID = -4065621590850621800L;

	public UserAlreadyRegisteredException(String name) {
		super("User " + name + " is already registered! Take another username!");
	}
}
