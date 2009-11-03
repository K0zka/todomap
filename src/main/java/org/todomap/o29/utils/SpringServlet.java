package org.todomap.o29.utils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.todomap.minigeoip.GeoipResolver;
import org.todomap.o29.logic.AttachmentService;
import org.todomap.o29.logic.BaseService;
import org.todomap.o29.logic.TodoService;
import org.todomap.o29.logic.UserService;

class SpringServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -722510994881678145L;
	TodoService todoService;
	BaseService baseService;
	UserService userService;
	AttachmentService attachmentService;
	GeoipResolver geoipResolver;

	@Override
	final public void init(ServletConfig config) throws ServletException {
		super.init(config);
		final WebApplicationContext webApplicationContext = WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
		todoService = (TodoService) webApplicationContext.getBean("todos");
		userService = (UserService) webApplicationContext.getBean("users");
		baseService = (BaseService) webApplicationContext.getBean("base");
		attachmentService = (AttachmentService) webApplicationContext.getBean("attachments");
		geoipResolver = (GeoipResolver) webApplicationContext.getBean("geoipResolver");
	}


}
