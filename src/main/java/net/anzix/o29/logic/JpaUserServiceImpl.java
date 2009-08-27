package net.anzix.o29.logic;

import net.anzix.o29.beans.User;

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

}
