package de.eva.rmi.chat;

import java.io.Serializable;

public interface Command extends Serializable {
	
	ChatClientService getClient();

	public static abstract class ClientCommand implements Command {

		private static final long serialVersionUID = 1772797458305432304L;
		private ChatClientService client;

		public ClientCommand(ChatClientService client) {
			this.client = client;
		}

		@Override
		public ChatClientService getClient() {
			return client;
		}

	}

	public static class ListCommand extends ClientCommand implements Serializable {

		private static final long serialVersionUID = -208782759178697890L;

		public ListCommand(ChatClientService client) {
			super(client);
		}
	}

	public static class ContentCommand extends ClientCommand implements Serializable {

		private static final long serialVersionUID = -1401696986635463460L;
		String content;

		public ContentCommand(ChatClientService client, String content) {
			super(client);
			this.content = content;
		}
		
		public String getContent() {
			return content;
		}
	}

	public static class ToSpecialUserCommand extends ContentCommand implements Serializable {

		private static final long serialVersionUID = 733354927624468964L;
		private String to;

		public ToSpecialUserCommand(ChatClientService client, String content, String to) {
			super(client, content);
			this.to = to;
		}

		public String getTo() {
			return to;
		}

	}

	public static class RegisterCommand extends ContentCommand implements Serializable {

		private static final long serialVersionUID = 9195234790611386872L;

		public RegisterCommand(ChatClientService c, String name) {
			super(c, name);
		}

	}
}