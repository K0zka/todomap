package org.todomap.prototypes.ajaxcleanup;

import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventChannelServlet extends WebSocketServlet {

	final TimeThread timeThread = new TimeThread();
	final JAXBContext context;
	final static Logger logger = LoggerFactory.getLogger(EventChannelServlet.class);

	public EventChannelServlet() throws JAXBException {
		super();
		context = JAXBContext.newInstance(this.getClass().getPackage().getName());
	}


	private final class WebSuket implements WebSocket {
		Outbound outbound = null;

		@Override
		public void onConnect(Outbound outbound) {
			System.out.println("connect");
			this.outbound = outbound;
			timeThread.add(outbound);
		}

		@Override
		public void onDisconnect() {
			System.out.println("disconnect");
			timeThread.remove(outbound);
		};

		@Override
		public void onFragment(final boolean arg0, final byte arg1,
				final byte[] arg2, int arg3, int arg4) {
			System.out.println("message fragment: ");
		}

		@Override
		public void onMessage(final byte arg0, final String msg) {
			System.out.println("message: " + msg);
			try {
				
				final Unmarshaller unmarshaller = context.createUnmarshaller();
				final Object message = unmarshaller.unmarshal(new StringReader(msg));
				
				logger.debug("msg:"+msg);
				logger.debug(message.getClass().getName());
				
			} catch (JAXBException e) {
				logger.error(e.getMessage(), e);
			}
		}

		@Override
		public void onMessage(final byte arg0, final byte[] data, int from,
				int to) {
			System.out.println("message: " + new String(data, from, to));
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1249099624196007384L;

	@Override
	protected WebSocket doWebSocketConnect(HttpServletRequest arg0, String arg1) {
		return new WebSuket();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		timeThread.start();
	}

}
