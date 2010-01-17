package org.todomap.o29.logic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.abdera.model.Base;
import org.springframework.orm.jpa.JpaCallback;
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

	@Override
	@SuppressWarnings("unchecked")
	public Rating getRating(final long id) {
		return (Rating) getJpaTemplate().execute(new JpaCallback() {

			@Override
			public Object doInJpa(final EntityManager em)
					throws PersistenceException {
				final Base data = getJpaTemplate().find(Base.class, id);
				List<Rating> resultList = em
						.createQuery(
								"select OBJECT(rating) from "
										+ Rating.class.getName()
										+ " rating where user = :user and bean = :bean")
						.setParameter("user", userService.getCurrentUser())
						.setParameter("bean", data).getResultList();
				return resultList.isEmpty() ? null : resultList.get(0);
			}
		});
	}

}
