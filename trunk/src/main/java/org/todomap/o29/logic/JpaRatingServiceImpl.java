package org.todomap.o29.logic;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.AnonRating;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.Rating;
import org.todomap.o29.beans.RatingReport;
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
				Object[] result = (Object[]) entityManager.createQuery(
						"select avg(rate), count(*) from "
								+ Rating.class.getName()
								+ " r where r.bean.id = :id").setParameter(
						"id", id).getSingleResult();
				Object[] anonResult = (Object[]) entityManager.createQuery(
						"select avg(rate), count(*) from "
								+ AnonRating.class.getName()
								+ " r where r.bean.id = :id").setParameter(
						"id", id).getSingleResult();

				final RatingSummary ret = new RatingSummary();
				ret.setAverage((Double) result[0]);
				ret.setNrOfRatings((Long) result[1]);
				ret.setAnonAverage((Double) anonResult[0]);
				ret.setNrOfAnonRatings((Long) anonResult[1]);
				ret
						.setBookmarked(((BigInteger) entityManager
								.createNativeQuery(
										"SELECT count(*) from o29user_base where bookmarks_id = ?")
								.setParameter(1, id).getSingleResult())
								.longValue());
				return ret;
			}
		});
	}

	@Override
	public RatingReport getAnonRatingReport(final long id) {
		return getJpaTemplate().execute(new JpaCallback<RatingReport>() {

			@Override
			public RatingReport doInJpa(final EntityManager entityManager)
					throws PersistenceException {
				final RatingReport ratingReport = new RatingReport();
				final List<Object[]> resultList = entityManager
						.createQuery(
								"select count(*), rate from "
										+ AnonRating.class.getName()
										+ " r where r.bean.id = :id group by r.rate order by r.rate")
						.setParameter("id", id).getResultList();
				for (final Object[] result : resultList) {
					final RatingReport.RatingReportItem item = new RatingReport.RatingReportItem();
					item.setValue((Short) result[1]);
					item.setCount((Long) result[0]);
					ratingReport.getItems().add(item);
				}
				return ratingReport;
			}

		});
	}

	@Override
	public RatingReport getRatingReport(final long id) {
		return getJpaTemplate().execute(new JpaCallback<RatingReport>() {

			@Override
			public RatingReport doInJpa(EntityManager entityManager)
					throws PersistenceException {
				return null;
			}

		});
	}

}
