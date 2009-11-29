package org.todomap.o29.utils.security;

import java.util.Date;


import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.todomap.o29.beans.User;
import org.todomap.o29.logic.UserService;

public class UserDetailServiceImpl implements UserDetailsService {

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
		public GrantedAuthority[] getAuthorities() {
			return new GrantedAuthority[]{};
		}

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
	}
	
	@Override
	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException, DataAccessException {
		User userByOpenIdUrl = userService.getUserByOpenIdUrl(username);
		if(userByOpenIdUrl == null) {
			userByOpenIdUrl = new User();
			userByOpenIdUrl.setCreated(new Date());
			userByOpenIdUrl.setOpenIdUrl(username);
			userService.persist(userByOpenIdUrl);
		}
		return new UserDetailsAdapter(userByOpenIdUrl);
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
