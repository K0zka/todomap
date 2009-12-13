package org.todomap.o29.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.Todo;

public class URLUtil {

	private final static Logger logger = Logger.getLogger(URLUtil.class);

	public static String getApplicationRoot(final HttpServletRequest request) {
		final StringBuffer requestURL = request.getRequestURL();
		final int contextLoc = requestURL.indexOf("WEB-INF");
		requestURL.replace(contextLoc, requestURL.length(), "");
		return requestURL.toString();
	}

	public static String getUrl(final HttpServletRequest request,
			final BaseBean bean) {
		return getApplicationRoot(request) + bean.getId() + "-"
				+ getTitle(bean) + ".html";
	}

	private static String getTitle(final BaseBean bean) {
		if (bean instanceof Todo) {
			try {
				return URLEncoder
						.encode(((Todo) bean).getShortDescr(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error(e);
			}
		}
		return bean.getClass().getSimpleName().toLowerCase();
	}
}
