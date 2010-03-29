package org.todomap.o29.logic;

import javax.servlet.http.HttpServletRequest;

import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.AnonRating;
import org.todomap.o29.utils.security.SecurityFilter;

public class JpaAnonimousRatingService extends JpaDaoSupport implements AnonimousRatingService{

	public JpaAnonimousRatingService(BaseService baseService) {
		super();
		this.baseService = baseService;
	}

	final BaseService baseService;
	
	@Override
	public void rate(final long id, final short value) {
		final HttpServletRequest request = SecurityFilter.requestThreadLocal.get();
		final AnonRating anonRating = new AnonRating();
		anonRating.setBean(baseService.getById(id));
		anonRating.setClient(request.getRemoteAddr());
		anonRating.setUserAgent(request.getHeader("User-agent"));
		getJpaTemplate().persist(anonRating);
	}

}
