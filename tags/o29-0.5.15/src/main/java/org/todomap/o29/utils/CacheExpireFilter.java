package org.todomap.o29.utils;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CacheExpireFilter implements Filter {

	/**
	 * Seconds to expire the image. One hour by default.
	 */
	private int secondsToExpire = 3600;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response,
			final FilterChain chain) throws IOException, ServletException {
		if(secondsToExpire != -1) {
			final GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());
			calendar.add(GregorianCalendar.SECOND, secondsToExpire);
			((HttpServletResponse)response).setDateHeader("Expires", calendar.getTimeInMillis());
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		final String strSecondsToExpire = filterConfig.getInitParameter("secondsToExpire");
		if(strSecondsToExpire != null) {
			secondsToExpire = Integer.parseInt(strSecondsToExpire);
		}
	}

}
