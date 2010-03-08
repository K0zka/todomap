package org.todomap.o29.logic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.Rating;
import org.todomap.o29.beans.RatingSummary;

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
				final BaseBean data = getJpaTemplate().find(BaseBean.class, id);
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

	@Override
	public RatingSummary getRatingSummary(final long id) {
		return getJpaTemplate().execute(new JpaCallback<RatingSummary>() {

			@Override
			public RatingSummary doInJpa(EntityManager entityManager)
					throws PersistenceException {
				Object[] result = (Object[]) entityManager.createQuery("select avg(rate), count(*) from "+Rating.class.getName()+" r where r.bean.id = :id").setParameter("id", id).getSingleResult();
				
				final RatingSummary ret = new RatingSummary();
				ret.setAverage((Double) result[0]);
				ret.setNrOfRatings((Long) result[1]);
				return ret;
			}
		});
	}

}
