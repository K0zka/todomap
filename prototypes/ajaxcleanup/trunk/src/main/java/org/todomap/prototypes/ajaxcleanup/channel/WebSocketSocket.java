package org.todomap.prototypes.ajaxcleanup.channel;

import java.io.IOException;

import javax.xml.bind.JAXBContext;

import org.eclipse.jetty.websocket.WebSocket.Outbound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketSocket implements Socket {

	public WebSocketSocket(final Outbound outbound, final JAXBContext context) {
		super();
		this.outbound = outbound;
		this.context = context;
	}

	final Outbound outbound;
	final JAXBContext context;
	final static Logger logger = LoggerFactory.getLogger(WebSocketSocket.class);

	@Override
	public void send(final String message) {
		try {
			outbound.sendMessage(message);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}
