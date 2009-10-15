package net.anzix.o29.logic;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import net.anzix.o29.beans.User;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

public class JpaUserServiceImpl extends JpaDaoSupport implements UserService {

	@Override
	public void addUser(User user) {
		getJpaTemplate().persist(user);
	}

	@Override
	public User getUserById(long id) {
		return getJpaTemplate().find(User.class, id);
	}

	@Override
	public User getUserByOpenIdUrl(final String url) {
		return (User) getJpaTemplate().execute(new JpaCallback() {
			
			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				return em.createQuery("select OBJECT(usr) from "+User.class.getName()+ " usr where openIdUrl = :url").setParameter("url", url).getSingleResult();
			}
		});
	}

}
