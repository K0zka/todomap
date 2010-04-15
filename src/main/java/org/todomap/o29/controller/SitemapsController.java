package org.todomap.o29.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.todomap.o29.logic.BaseService;

public class SitemapsController implements Controller {
	public SitemapsController(BaseService baseService) {
		super();
		this.baseService = baseService;
	}

	final BaseService baseService;

	@Override
	public ModelAndView handleRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		final ModelAndView modelAndView = new ModelAndView(request.getRequestURI().endsWith("sitemaps.xml") ? "sitemaps.super.xml" : "sitemaps.xml");
		final Locale locale = (Locale) request.getAttribute("locale");
		modelAndView.addObject("dates", baseService.listActiveDays(locale
				.getCountry()));
		return modelAndView;
	}

}
