package org.todomap.o29.logic;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.todomap.o29.beans.User;

public class JpaHomeServiceImpl extends JpaDaoSupport implements HomeService {

	private final static Logger logger = LoggerFactory
			.getLogger(JpaHomeServiceImpl.class);

	UserService userService;

	@Override
	public HomeLocation getHome() {
		return new HomeLocation();
	}

	@Override
	public void setHome(HomeLocation loc) {
		logger.info("Update loc");
	}

	@Override
	public boolean isAuthenticated() {
		return getAuthentication() instanceof OpenIDAuthenticationToken;
	}

	@Override
	public User getUser() {
		return userService.getCurrentUser();
	}
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void setUserData(final User user) {
		final Authentication authentication = getAuthentication();
		if(authentication != null) {
			//I suppose the user exists, since authenticated
			final User userByOpenId = userService.getUserByOpenIdUrl(authentication.getName());
			userByOpenId.setEmail(user.getEmail());
			userByOpenId.setDisplayName(user.getDisplayName());
			userByOpenId.setHomeLoc(user.getHomeLoc());
			userByOpenId.setHomeZoomLevel(user.getHomeZoomLevel());
			
			userService.persist(userByOpenId);
		}
	}

	private Authentication getAuthentication() {
		return SecurityContextHolder
		.getContext().getAuthentication();
	}

}
