package org.todomap.o29.logic;


import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.User;

public class JpaBaseService extends JpaDaoSupport implements BaseService {

	UserService userService;
	
	@Override
	public BaseBean getById(final long id) {
		return getJpaTemplate().find(BaseBean.class, id);
	}

	@Override
	public BaseBean removebyId(long id) {
		final User currentUser = userService.getCurrentUser();
		final BaseBean bean = getById(id);
		final String openIdUrl = bean.getCreator().getOpenIdUrl();
		if(openIdUrl != null && openIdUrl.equals(currentUser.getOpenIdUrl())) {
			getJpaTemplate().remove(bean);
		}
		return bean;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public List<BaseBean> list(final String countryCode, final Date day) {
		return getJpaTemplate().execute(new JpaCallback<List<BaseBean>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<BaseBean> doInJpa(final EntityManager entityManager)
					throws PersistenceException {
				return entityManager.createQuery("select object (b) from " + BaseBean.class.getName()+ " b where date_trunc('day',b.created) = :day").setParameter("day", day).getResultList();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Date> listActiveDays(String countryCode) {
		return getJpaTemplate().executeFind(new JpaCallback<List<Date>>() {

			@Override
			public List<Date> doInJpa(final EntityManager entityManager)
					throws PersistenceException {
				//TODO: this runs on postgresql only
				return entityManager.createNativeQuery("SELECT distinct date_trunc('day',created) from base").getResultList();
			}
		});
	}

}
