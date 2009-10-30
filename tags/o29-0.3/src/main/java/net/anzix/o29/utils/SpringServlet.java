package net.anzix.o29.utils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.anzix.o29.logic.AttachmentService;
import net.anzix.o29.logic.BaseService;
import net.anzix.o29.logic.TodoService;
import net.anzix.o29.logic.UserService;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

class SpringServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -722510994881678145L;
	TodoService todoService = null;
	BaseService baseService = null;
	UserService userService = null;
	AttachmentService attachmentService = null;

	@Override
	final public void init(ServletConfig config) throws ServletException {
		super.init(config);
		final WebApplicationContext webApplicationContext = WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
		todoService = (TodoService) webApplicationContext.getBean("todos");
		userService = (UserService) webApplicationContext.getBean("users");
		baseService = (BaseService) webApplicationContext.getBean("base");
		attachmentService = (AttachmentService) webApplicationContext.getBean("attachments");
	}


}
