package org.todomap.o29.utils;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.todomap.o29.beans.Todo;


public class RegionRssServlet extends SpringServlet {

	private static final long serialVersionUID = 212173477860116511L;

	private final static Logger logger = LoggerFactory.getLogger(RegionRssServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final String[] split = req.getRequestURI().replace("/rss.xml/region/", "").split("/");
		final String countryCode = getParam(split, 0);
		final String state = getParam(split, 1);
		final String town = getParam(split, 2);
		final List<Todo> todos = todoService.getByLocation(countryCode, state,
				town);
		req.setAttribute("chanelLink", req.getRequestURL().toString());
		req.setAttribute("todos", todos);
		req.setAttribute("countryCode", countryCode);
		req.setAttribute("state", state);
		req.setAttribute("town", town);
		req.getRequestDispatcher("/WEB-INF/jsp/town-todorss.jsp").forward(req,
				resp);
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
