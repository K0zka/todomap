package org.todomap.o29.logic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.todomap.o29.beans.User;

public class JpaUserServiceImpl extends JpaDaoSupport implements UserService {

	@Override
	public void persist(final User user) {
		getJpaTemplate().persist(user);
	}

	@Override
	public User getUserById(long id) {
		return getJpaTemplate().find(User.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public User getUserByOpenIdUrl(final String url) {
		return (User) getJpaTemplate().execute(new JpaCallback() {
			
			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				List<User> resultList = em.createQuery("select OBJECT(usr) from "+User.class.getName()+ " usr where openIdUrl = :url").setParameter("url", url).getResultList();
				return resultList.size() == 0 ? null : resultList.get(0);
			}
		});
	}

	@Override
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication == null ? null : getUserByOpenIdUrl(authentication.getName());
	}

}
