package org.todomap.o29.logic;

import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.Rating;

public class JpaRatingServiceImpl extends JpaDaoSupport implements
		RatingService {

	UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void addRating(final long id, final Rating rating) {
		final BaseBean bean = getJpaTemplate().find(BaseBean.class, id);
		rating.setBean(bean);
		rating.setUser(userService.getCurrentUser());
		getJpaTemplate().persist(rating);
	}

}
