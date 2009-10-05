package net.anzix.o29.utils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.anzix.o29.logic.TodoService;

import org.springframework.web.context.support.WebApplicationContextUtils;

class SpringServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -722510994881678145L;
	TodoService todoService = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		todoService = (TodoService) WebApplicationContextUtils
				.getWebApplicationContext(getServletContext()).getBean("todos");
	}


}
