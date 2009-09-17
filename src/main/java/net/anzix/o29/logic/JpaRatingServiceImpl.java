package net.anzix.o29.logic;

import net.anzix.o29.beans.BaseBean;
import net.anzix.o29.beans.Rating;

import org.springframework.orm.jpa.support.JpaDaoSupport;

public class JpaRatingServiceImpl extends JpaDaoSupport implements
		RatingService {

	@Override
	public void addRating(final long id, final Rating rating) {
		final BaseBean bean = getJpaTemplate().find(BaseBean.class, id);
		rating.setBean(bean);
		getJpaTemplate().persist(rating);
	}

}
