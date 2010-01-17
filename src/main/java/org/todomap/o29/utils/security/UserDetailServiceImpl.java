package org.todomap.o29.utils.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.todomap.o29.beans.User;
import org.todomap.o29.logic.UserService;

public class UserDetailServiceImpl implements UserDetailsService, AuthenticationSuccessHandler {

	private UserService userService;

	class UserDetailsAdapter implements UserDetails {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1834973263531192130L;

		public UserDetailsAdapter(final User user) {
			super();
			this.user = user;
		}

		final User user;

		@Override
		public String getPassword() {
			return "";
		}

		@Override
		public String getUsername() {
			return user.getOpenIdUrl();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

		@Override
		public Collection<GrantedAuthority> getAuthorities() {
			return new ArrayList<GrantedAuthority>();
		}
	}
	
	@Override
	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException, DataAccessException {
		return new UserDetailsAdapter(makeUser(username));
	}

	private User makeUser(final String username) {
		User userByOpenIdUrl = userService.getUserByOpenIdUrl(username);
		if(userByOpenIdUrl == null) {
			userByOpenIdUrl = new User();
			userByOpenIdUrl.setCreated(new Date());
			userByOpenIdUrl.setOpenIdUrl(username);
			userService.persist(userByOpenIdUrl);
		}
		return userByOpenIdUrl;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) throws IOException,
			ServletException {
		final User user = makeUser(authentication.getName());
		for( OpenIDAttribute attribute : ((OpenIDAuthenticationToken)authentication).getAttributes()) {
			if("name".equals(attribute.getName()) && StringUtils.isBlank(user.getName())) {
				user.setDisplayName(firstValueFrom(attribute));
			}
			if("email".equals(attribute.getName()) && StringUtils.isBlank(user.getEmail())) {
				user.setEmail(firstValueFrom(attribute));
			}
		}
		response.sendRedirect("/");
	}

	private String firstValueFrom(final OpenIDAttribute attribute) {
		if(attribute.getValues() == null || attribute.getValues().isEmpty()) {
			return null;
		} else {
			return attribute.getValues().get(0);
		}
	}

}
