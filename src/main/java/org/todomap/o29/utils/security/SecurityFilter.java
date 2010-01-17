package org.todomap.o29.utils.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityFilter implements Filter {

	private final static Logger logger = LoggerFactory
			.getLogger(SecurityFilter.class);

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {

		final Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		final String method = ((HttpServletRequest) request).getMethod();
		final boolean protectedMethod = isProtectedMethod(method);

		if (authentication instanceof AnonymousAuthenticationToken && protectedMethod) {
			((HttpServletResponse) response)
					.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			((HttpServletResponse) response).setContentType("application/json");
			response.getWriter().write("{errors:['login-required']}");
		} else {
			logger.debug(authentication instanceof AnonymousAuthenticationToken ? "[anon]" : authentication.getName());
			chain.doFilter(request, response);
		}

	}

	private boolean isProtectedMethod(final String method) {
		return ("POST".equalsIgnoreCase(method)
				|| "PUT".equalsIgnoreCase(method) || "DELETE"
				.equalsIgnoreCase(method));
	}

	@Override
	public void init(final FilterConfig config) throws ServletException {
	}

}
