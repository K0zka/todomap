package org.todomap.o29.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.todomap.o29.beans.Todo;

public class TodoFullPageServlet extends SpringServlet {

	private final static Logger logger = LoggerFactory
			.getLogger(TodoFullPageServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 8667781743381799117L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			final long todoId = extractTodoId(req.getRequestURI());
			logger.debug("Requested todo" + todoId);
			final Todo todo = todoService.getById(todoId);
			if(todo != null) {
				logger.debug("Found todo:" + todo.getShortDescr());
				req.setAttribute("todo", todo);
				req.getRequestDispatcher("/WEB-INF/jsp/todo.jsp").forward(req, resp);
			} else {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (NumberFormatException nfe) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	long extractTodoId(final String requestURI) {
		final int slashPos = requestURI.lastIndexOf('/');
		final int separatorPos = slashPos == -1 ? requestURI.indexOf('-')
				: requestURI.indexOf('-', slashPos);
		if (separatorPos == -1) {
			return 0;
		} else {
			String idStr = requestURI.substring(slashPos == -1 ? 0 : slashPos + 1, separatorPos);
			return Long.parseLong(idStr);
		}
	}

}
