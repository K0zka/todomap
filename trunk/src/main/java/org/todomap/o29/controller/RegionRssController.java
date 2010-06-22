package org.todomap.o29.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.todomap.o29.beans.Todo;
import org.todomap.o29.logic.TodoService;

public class RegionRssController implements Controller{
	private final static Logger logger = LoggerFactory.getLogger(RegionRssController.class);

	final TodoService todoService;
	
	public RegionRssController(TodoService todoService) {
		super();
		this.todoService = todoService;
	}
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final ModelAndView mav = new ModelAndView("town-todorss");
		final String[] split = request.getRequestURI().replace("/rss.xml/region/", "").split("/");
		final String countryCode = getParam(split, 0);
		final String state = getParam(split, 1);
		final String town = getParam(split, 2);
		final List<Todo> todos = todoService.getByLocation(countryCode, state,
				town);
		mav.addObject("chanelLink", request.getRequestURL().toString());
		mav.addObject("todos", todos);
		mav.addObject("countryCode", countryCode);
		mav.addObject("state", state);
		mav.addObject("town", town);
		return mav;
	}
	static String getParam(final String[] split, final int index) {
		if (split.length > index) {
			try {
				return URIUtil.decode(split[index]);
			} catch (final URIException e) {
				logger.error("Could not decode:"+split[index], e);
				return null;
			}
		} else {
			return null;
		}
	}

}
