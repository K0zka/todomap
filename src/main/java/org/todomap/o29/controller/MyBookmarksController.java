package org.todomap.o29.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.todomap.o29.logic.UserService;

public class MyBookmarksController implements Controller {

	public MyBookmarksController(UserService userService) {
		super();
		this.userService = userService;
	}

	final UserService userService;
	
	@Override
	public ModelAndView handleRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView("embed/mybookmarks");
		mav.addObject("user", userService.getUserById(getId(request)));
		return mav;
	}

	private long getId(HttpServletRequest request) {
		return Long.parseLong( StringUtils.substringBefore(StringUtils.substringAfterLast(request.getRequestURI(), "/"),".html"));
	}

}
