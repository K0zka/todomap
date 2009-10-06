package net.anzix.o29.utils.security;

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
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

public class SecurityFilter implements Filter {

	private final static Logger logger = LoggerFactory
			.getLogger(SecurityFilter.class);

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		final String method = ((HttpServletRequest) request).getMethod();
		if (authentication == null
				&& ("POST".equalsIgnoreCase(method)
						|| "PUT".equalsIgnoreCase(method) || "DELETE"
						.equalsIgnoreCase(method))) {
			((HttpServletResponse) response)
					.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			((HttpServletResponse) response).setContentType("application/json");
			response.getWriter().write("{errors:['login-required']}");
		} else {
			logger.debug(authentication == null ? "[anon]" : authentication.getName());
			chain.doFilter(request, response);
		}

	}

	@Override
	public void init(FilterConfig config) throws ServletException {

	}

}
