package org.todomap.prototypes.ajaxcleanup;

import java.io.StringReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.todomap.prototypes.ajaxcleanup.channel.MessengerImpl;
import org.todomap.prototypes.ajaxcleanup.channel.WebSocketSocket;
import org.todomap.prototypes.ajaxcleanup.messages.SubscribeMessage;

@WebServlet(urlPatterns = { "/ec-websocket" })
public class EventChannelServlet extends WebSocketServlet {

	final JAXBContext context;
	final static Logger logger = LoggerFactory
			.getLogger(EventChannelServlet.class);

	public EventChannelServlet() throws JAXBException {
		super();
		context = JAXBContext.newInstance(SubscribeMessage.class.getPackage()
				.getName());
	}

	private final class WebSuket implements WebSocket {

		public WebSuket(MessengerImpl messengerImpl) {
			super();
			this.messengerImpl = messengerImpl;
		}

		final MessengerImpl messengerImpl;
		WebSocketSocket socket = null;

		@Override
		public void onConnect(Outbound outbound) {
			logger.debug("connect");
			socket = new WebSocketSocket(outbound, context);
			messengerImpl.addSocket(socket);
		}

		@Override
		public void onDisconnect() {
			logger.debug("disconnect");
			messengerImpl.removeSocket(socket);
		};

		@Override
		public void onMessage(final byte arg0, final String msg) {
			logger.debug("message: " + msg);
			try {

				final Unmarshaller unmarshaller = context.createUnmarshaller();
				final Object message = unmarshaller.unmarshal(new StringReader(
						msg));

				logger.debug("msg:" + msg);
				logger.debug(message.getClass().getName());

			} catch (JAXBException e) {
				logger.error(e.getMessage(), e);
			}
		}

		@Override
		public void onMessage(final byte arg0, final byte[] data, int from,
				int to) {
			logger.debug("message: " + new String(data, from, to));
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1249099624196007384L;

	@Override
	protected WebSocket doWebSocketConnect(HttpServletRequest request,
			String arg1) {
		return new WebSuket((MessengerImpl) WebApplicationContextUtils
				.getRequiredWebApplicationContext(request.getServletContext())
				.getBean("messenger"));
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

}
