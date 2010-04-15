package org.todomap.o29.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.todomap.o29.logic.BaseService;

public class LinksForTheDayController implements Controller {

	public LinksForTheDayController(BaseService baseService) {
		super();
		this.baseService = baseService;
	}

	final BaseService baseService;

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final Date day = new SimpleDateFormat("yyyy-MM-dd").parse(request
				.getRequestURI().replace(".txt", "").replace("/sitemaps/", ""));
		final Locale locale = (Locale) request.getAttribute("locale");
		final ModelAndView modelAndView = new ModelAndView(request
				.getRequestURI().endsWith("txt") ? "links.txt"
				: "sitemap.day.xml");
		modelAndView.addObject("beans", baseService.list(locale.getCountry(),
				day));
		return modelAndView;
	}

}
