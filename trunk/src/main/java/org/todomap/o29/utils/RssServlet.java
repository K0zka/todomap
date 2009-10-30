package org.todomap.o29.utils;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.todomap.o29.beans.Todo;


public class RssServlet extends SpringServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2450066955274418467L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
 		final String requestUri = req.getRequestURI();
		final int lastSlash = requestUri.lastIndexOf('/');
		final String paramStr = requestUri.substring(lastSlash + 1);
		final String[] params = paramStr.split(",");
		final double swx = Double.parseDouble(params[0]);
		final double swy = Double.parseDouble(params[1]);
		final double nex = Double.parseDouble(params[2]);
		final double ney = Double.parseDouble(params[3]);
		final List<Todo> todos = todoService.getTodos(ney, nex, swy, swx);
		req.setAttribute("todos", todos);
		req.getRequestDispatcher("/WEB-INF/jsp/todorss.jsp").forward(req, resp);
	}

}
