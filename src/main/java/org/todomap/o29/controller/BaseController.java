package org.todomap.o29.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.logic.BaseService;
import org.todomap.o29.utils.URLUtil;

public final class BaseController implements Controller {
	
	public BaseController(BaseService baseService, String successView) {
		super();
		this.baseService = baseService;
		this.successView = successView;
	}

	final BaseService baseService;
	final String successView;

	@Override
	public final ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		final long itemId = URLUtil.extractId(request.getRequestURI());
		if(itemId == 0) {
			baseService.getById(itemId);
			return new ModelAndView("notfound");
		}  else {
			final BaseBean baseBean = baseService.getById(itemId);
			final ModelAndView modelAndView = new ModelAndView(successView);
			modelAndView.addObject("data", baseBean);
			return modelAndView;
		}
	}
}
