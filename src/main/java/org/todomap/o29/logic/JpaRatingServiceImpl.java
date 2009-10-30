package org.todomap.o29.logic;


import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.Rating;

public class JpaRatingServiceImpl extends JpaDaoSupport implements
		RatingService {

	@Override
	public void addRating(final long id, final Rating rating) {
		final BaseBean bean = getJpaTemplate().find(BaseBean.class, id);
		rating.setBean(bean);
		getJpaTemplate().persist(rating);
	}

}
