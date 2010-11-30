package org.todomap.prototypes.ajaxcleanup.channel;

public interface Messenger {
	void message(Object message);
	void addSocket(Socket socket);
	void removeSocket(Socket socket);
}
