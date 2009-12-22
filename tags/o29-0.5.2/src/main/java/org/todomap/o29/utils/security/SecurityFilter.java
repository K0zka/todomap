package org.todomap.o29.utils.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.todomap.o29.beans.User;
import org.todomap.o29.logic.UserService;

public class SecurityFilter implements Filter {

	private final static Logger logger = LoggerFactory
			.getLogger(SecurityFilter.class);

	private ServletContext servletContext;

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

		checkUser(authentication); 
		
		if (authentication == null && protectedMethod) {
			((HttpServletResponse) response)
					.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			((HttpServletResponse) response).setContentType("application/json");
			response.getWriter().write("{errors:['login-required']}");
		} else {
			logger.debug(authentication == null ? "[anon]" : authentication.getName());
			chain.doFilter(request, response);
		}

	}

	private void checkUser(final Authentication authentication) {
		/* check user in DB */
		if(authentication != null) {
			final String openIdUrl = authentication.getName();
			UserService userService = getUserService();
			User userByOpenIdUrl = userService.getUserByOpenIdUrl(openIdUrl);
			if(userByOpenIdUrl == null) {
				userByOpenIdUrl = new User();
				userByOpenIdUrl.setOpenIdUrl(openIdUrl);
				userService.persist(userByOpenIdUrl);
			}
		}
	}

	private UserService getUserService() {
		return (UserService) WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext).getBean("users");
	}

	private boolean isProtectedMethod(final String method) {
		return ("POST".equalsIgnoreCase(method)
				|| "PUT".equalsIgnoreCase(method) || "DELETE"
				.equalsIgnoreCase(method));
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.servletContext = config.getServletContext();
	}

}
