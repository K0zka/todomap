package org.todomap.o29.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.todomap.o29.beans.Todo;
import org.todomap.o29.logic.TodoService;

public class RssController implements Controller{

	public RssController(TodoService todoService) {
		super();
		this.todoService = todoService;
	}

	final TodoService todoService;
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
 		final String requestUri = request.getRequestURI();
		final int lastSlash = requestUri.lastIndexOf('/');
		final String paramStr = requestUri.substring(lastSlash + 1);
		final String[] params = paramStr.split(",");
		final double swx = Double.parseDouble(params[0]);
		final double swy = Double.parseDouble(params[1]);
		final double nex = Double.parseDouble(params[2]);
		final double ney = Double.parseDouble(params[3]);
		final List<Todo> todos = todoService.getTodos(ney, nex, swy, swx);
		final ModelAndView mav = new ModelAndView("todorss");
		mav.addObject("chanelLink", request.getRequestURL().toString());
		mav.addObject("todos", todos);
		return mav;
	}

}
