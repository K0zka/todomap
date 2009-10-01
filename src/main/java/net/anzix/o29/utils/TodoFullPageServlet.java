package net.anzix.o29.utils;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anzix.o29.beans.Todo;
import net.anzix.o29.logic.TodoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class TodoFullPageServlet extends HttpServlet {

	TodoService todoService = null;

	private final static Logger logger = LoggerFactory
			.getLogger(TodoFullPageServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 8667781743381799117L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		final long todoId = extractTodoId(req.getRequestURI());
		logger.debug("Requested todo" + todoId);
		final Todo todo = todoService.getById(todoId);
		logger.debug("Found todo:" + todo.getShortDescr());
		req.setAttribute("todo", todo);
		req.getRequestDispatcher("/WEB-INF/jsp/todo.jsp").forward(req, resp);
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

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		todoService = (TodoService) WebApplicationContextUtils
				.getWebApplicationContext(getServletContext()).getBean("todos");
	}

}
