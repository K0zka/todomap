package org.todomap.prototypes.ajaxcleanup;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns={"/ec-send"})
public class LongPollSendServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7653298435991178403L;

	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		
	}

}
