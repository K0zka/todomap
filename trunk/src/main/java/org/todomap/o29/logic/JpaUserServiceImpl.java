package org.todomap.o29.logic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.todomap.o29.beans.Link;
import org.todomap.o29.beans.Todo;
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
		return getJpaTemplate().execute(new JpaCallback<User>() {
			
			@Override
			public User doInJpa(EntityManager em) throws PersistenceException {
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

	@Override
	public long addUserLink(Link link) {
		final User currentUser = getCurrentUser();
		link.setLinkOwner(currentUser);
		getJpaTemplate().persist(link);
		return link.getId();
	}

	@Override
	public List<Link> listUserLinks() {
		return getCurrentUser().getUserLinks();
	}

	@Override
	public long removeLink(long id) {
		final User currentUser = getCurrentUser();
		for(Link link : currentUser.getUserLinks()) {
			if(link.getId() == id) {
				getJpaTemplate().remove(link);
				return id;
			}
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Todo> listTodoBookmarks(final User user) {
		return getJpaTemplate().find("select object(todo) from "+Todo.class.getName()+ " todo where ? in elements(todo.listeners)", user);
	}

}
