package org.todomap.o29.logic;

import javax.servlet.http.HttpServletRequest;

import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.AnonRating;
import org.todomap.o29.beans.RatingSummary;
import org.todomap.o29.utils.security.SecurityFilter;

public class JpaAnonRatingService extends JpaDaoSupport implements AnonRatingService{

	public RatingService ratingService = null;

	public RatingService getRatingService() {
		return ratingService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public void setRatingService(RatingService ratingService) {
		this.ratingService = ratingService;
	}

	public BaseService getBaseService() {
		return baseService;
	}

	BaseService baseService;
	
	@Override
	public RatingSummary addRate(final long id, final short value) {
		final HttpServletRequest request = SecurityFilter.requestThreadLocal.get();
		final AnonRating anonRating = new AnonRating();
		anonRating.setBean(baseService.getById(id));
		anonRating.setClient(request.getRemoteAddr());
		anonRating.setUserAgent(request.getHeader("User-agent"));
		anonRating.setRate(value);
		getJpaTemplate().persist(anonRating);
		return ratingService.getRatingSummary(id);
	}

}
