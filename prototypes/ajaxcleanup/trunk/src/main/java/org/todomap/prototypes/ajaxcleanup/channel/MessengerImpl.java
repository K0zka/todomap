package org.todomap.prototypes.ajaxcleanup.channel;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.todomap.prototypes.ajaxcleanup.messages.UpdateMessage;

public class MessengerImpl implements Messenger {

	public MessengerImpl() throws JAXBException {
		super();
		context = JAXBContext.newInstance(UpdateMessage.class.getPackage().getName());
	}

	final ArrayList<Socket> sockets = new ArrayList<Socket>();
	final JAXBContext context;
	final static Logger logger = LoggerFactory.getLogger(MessengerImpl.class);
	
	@Override
	public void message(Object message) {
		final String strMsg = serialize(message);
		for(final Socket socket : sockets) {
			socket.send(strMsg);
		}
	}

	String serialize(final Object message) {
		try {
			final StringWriter writer = new StringWriter();
			context.createMarshaller().marshal(message, writer);
			return writer.toString();
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public void addSocket(Socket socket) {
		sockets.add(socket);
	}

	@Override
	public void removeSocket(Socket socket) {
		sockets.remove(socket);
	}

}
