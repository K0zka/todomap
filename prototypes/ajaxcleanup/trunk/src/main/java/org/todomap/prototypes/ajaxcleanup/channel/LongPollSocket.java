package org.todomap.prototypes.ajaxcleanup.channel;

import java.io.IOException;
import java.util.Queue;

import javax.servlet.AsyncContext;

import org.eclipse.jetty.util.ArrayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LongPollSocket implements Socket {

	public LongPollSocket(String clientId) {
		super();
		this.clientId = clientId;
	}

	AsyncContext context;
	final String clientId;
	final Queue<String> messages = new ArrayQueue<String>();
	final long started = System.currentTimeMillis();
	final static Logger logger = LoggerFactory.getLogger(LongPollSocket.class);

	@Override
	public void send(final String message) {
		try {
			if(context == null || context.getTimeout() + started > System.currentTimeMillis()) {
				messages.add(message);
			} else {
				context.getResponse().getOutputStream().print(message.toString());
			}
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public Queue<String> getMessages() {
		return messages;
	}

	public void setContext(AsyncContext context) {
		this.context = context;
	}

}
