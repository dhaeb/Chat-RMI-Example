package de.eva.rmi.chat;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = -8180676753420606377L;
	
	private String from;
	private String content;
	
	public Message(String from, String content) {
		super();
		this.from = from;
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public String getContent() {
		return content;
	}
	
	
}
