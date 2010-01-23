package org.todomap.o29.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GeoipServlet extends SpringServlet {

	private static final long serialVersionUID = 4180666820704962665L;

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		final String countryCode = geoipResolver.getCountryName(req.getRemoteAddr());
		final PrintWriter writer = resp.getWriter();
		resp.setHeader("Cache-control", "max-age=3600");
		if(req.getRequestURI().endsWith(".js")) {
			writer.write("org.todomap.geoip='");
			writer.write(countryCode == null ? "unknown" : countryCode);
			writer.write("';\n");
		} else {
			writer.write(countryCode == null ? "unknown" : countryCode);
		}
	}

}
