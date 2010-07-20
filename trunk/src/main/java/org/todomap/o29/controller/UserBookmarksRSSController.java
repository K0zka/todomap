package org.todomap.o29.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.todomap.o29.beans.User;
import org.todomap.o29.logic.UserService;

public class UserBookmarksRSSController implements Controller {
	
	private final UserService userService;
	
	public UserBookmarksRSSController(UserService userService) {
		super();
		this.userService = userService;
	}

	@Override
	public ModelAndView handleRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		
		final ModelAndView mav = new ModelAndView("userbookmarksrss");
		final User user = userService.getUserById(getId(request));
		mav.addObject("chanelLink", request.getRequestURL().toString());
		mav.addObject("user", user);
		mav.addObject("todos", userService.listTodoBookmarks(user));
		return mav;
	}

	private long getId(HttpServletRequest request) {
		return Long.parseLong(StringUtils.substringAfterLast(request.getRequestURI(), "/"));
	}

}
