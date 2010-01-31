package org.todomap.o29.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

public class LanguageFilter implements Filter {

	private final Map<String, Locale> locales = new HashMap<String, Locale>();

	public Locale getLocaleFromRequest(final HttpServletRequest request) {
		final String serverName = request.getServerName();
		final int dotPos = serverName.indexOf('.');
		final String languageCode = dotPos < 0 ? null : serverName
				.substring(0,dotPos);
		return locales.containsKey(languageCode) ? locales.get(languageCode)
				: locales.get("default");
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		request.setAttribute("locale", getLocaleFromRequest((HttpServletRequest) request));
		chain.doFilter(request, response);
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		final Properties properties = new Properties();
		final InputStream stream = Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("l10n.properties");
		try {
			properties.load(stream);
			for(final Object key : properties.keySet()) {
				final String localeString = properties.getProperty((String) key);
				final String[] split = localeString.split("_");
				locales.put((String) key, new Locale(split[0], split[1]));
			}

		} catch (IOException e) {
			throw new ServletException(e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

}
