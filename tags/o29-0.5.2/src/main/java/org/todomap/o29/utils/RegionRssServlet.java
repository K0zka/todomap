package org.todomap.o29.utils;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.todomap.o29.beans.Todo;


public class RegionRssServlet extends SpringServlet {

	private static final long serialVersionUID = 212173477860116511L;

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

	static String getParam(String[] split, int index) {
		if (split.length > index) {
			return split[index];
		} else {
			return null;
		}
	}

}
