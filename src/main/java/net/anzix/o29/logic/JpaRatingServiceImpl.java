package net.anzix.o29.logic;

import net.anzix.o29.beans.BaseBean;
import net.anzix.o29.beans.Rating;

import org.springframework.orm.jpa.support.JpaDaoSupport;

public class JpaRatingServiceImpl extends JpaDaoSupport implements
		RatingService {

	@Override
	public void addRating(long id, short rating, String comment) {
		BaseBean bean = getJpaTemplate().find(BaseBean.class, id);
		final Rating ratingBean = new Rating();
		ratingBean.setBean(bean);
		ratingBean.setRate(rating);
		ratingBean.setComment(comment);
		getJpaTemplate().persist(ratingBean);
	}

}
