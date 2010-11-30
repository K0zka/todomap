package org.todomap.prototypes.ajaxcleanup;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.todomap.prototypes.ajaxcleanup.channel.LongPollSocket;
import org.todomap.prototypes.ajaxcleanup.channel.Messenger;

@WebServlet(urlPatterns={"/ec-poll"}, asyncSupported=true)
public class LongPollFetchServlet extends HttpServlet {

	private static final long serialVersionUID = -6212932063655626507L;

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		final LongPollSocket socket = getSocket(request);

		if(!socket.getMessages().isEmpty()) {
			//if there are already messages, send them to the client
			response.getWriter().write("<messages>");
			while(!socket.getMessages().isEmpty()) {
				response.getWriter().write(socket.getMessages().poll());
			}
			response.getWriter().write("</messages>");
		} else {
			//if no messages right now, then let's just wait for them
			request.isAsyncSupported();
			final AsyncContext context = request.startAsync();
			context.setTimeout(60000);
			socket.setContext(context);
		}
	}

	private LongPollSocket getSocket(final HttpServletRequest request) {
		final Messenger messenger = (Messenger) WebApplicationContextUtils
				.getRequiredWebApplicationContext(request.getServletContext())
				.getBean("messenger");
		final LongPollSocket socket = new LongPollSocket(request.getSession(true).getId());
		messenger.addSocket(socket);
		return socket;
	}

}
