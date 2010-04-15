package org.todomap.o29.utils.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Filter to save parameters to session before an action happens.
 * 
 * Well, this is may not be very nice here, but this filter will be used to
 * save map position and zoom level before the user logs in.
 * 
 * @author kocka
 *
 */
public class RememberParamsFilter implements Filter {

	/** the names of the parameters to save */
	private String[] paramsToSave;
	/** the name of the trigger parameter */
	private String triggerParam;
	/** the value of the trigger parameter */
	private String triggerParamValue;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException {
		
		if(triggerParamValue.equals(request.getParameter(triggerParam))) {
			final HttpSession session = ((HttpServletRequest)request).getSession(true);
			for(final String param : paramsToSave) {
				session.setAttribute(param, request.getParameter(param));
			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		paramsToSave = filterConfig.getInitParameter("paramsToSave").split(",");
		triggerParam = filterConfig.getInitParameter("triggerParam");
		triggerParamValue = filterConfig.getInitParameter("triggerParamValue");
	}
	
}
