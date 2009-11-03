package org.todomap.o29.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GeoipServlet extends SpringServlet {

	private static final long serialVersionUID = 4180666820704962665L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final String countryCode = geoipResolver.getCountryCode(req.getRemoteAddr());
		resp.getWriter().write(countryCode == null ? "unknown" : countryCode);
	}

}
