package org.todomap.o29.logic;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.Locatable;
import org.todomap.o29.beans.User;

public class JpaBookmarkService extends JpaDaoSupport implements BookmarkService {

	UserService userService;
	BaseService baseService;
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public User addBookmark(final long itemId) {
		final User currentUser = userService.getCurrentUser();
		currentUser.getBookmarks().add(baseService.getById(itemId));
		getJpaTemplate().persist(currentUser);
		return currentUser;
	}

	@Override
	public List<Bookmark> bookmarks() {
		final ArrayList<Bookmark> ret = new ArrayList<Bookmark>();
		final User currentUser = userService.getCurrentUser();
		if(currentUser != null) {
			for(final BaseBean bean : currentUser.getBookmarks()) {
				final Bookmark bookmark = new Bookmark();
				bookmark.setId(bean.getId());
				bookmark.setVersion(bean.getVersion());
				bookmark.setCreated(bean.getCreated());
				bookmark.setText(bean.getName());
				if(bean instanceof Locatable) {
					bookmark.setCoordinate(((Locatable)bean).getLocation());
				}
				ret.add(bookmark);
			}
		}
		return ret;
	}

	@Override
	public void removeBookmark(long itemId) {
		final User currentUser = userService.getCurrentUser();
		currentUser.getBookmarks().remove(baseService.getById(itemId));
		getJpaTemplate().persist(currentUser);
	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	public boolean isBookmarked(long todoId) {
		final User currentUser = userService.getCurrentUser();
		//FIXME: this is suboptimal on big number of bookmarks. jpa query would be nice
		if(currentUser == null) {
			return false;
		} else {
			for(final BaseBean bean : currentUser.getBookmarks()) {
				if(bean.getId() == todoId) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public List<ListenerUser> getListeners(final long todoId) {
		final BaseBean baseBean = getJpaTemplate().find(BaseBean.class, todoId);
		return getJpaTemplate().execute(new JpaCallback<List<ListenerUser>>() {

			@Override
			public List<ListenerUser> doInJpa(EntityManager manager)
					throws PersistenceException {
				
				@SuppressWarnings("unchecked")
				List<Object[]> results = manager.createQuery("select u.id, u.displayName from "+User.class.getName()+" u where :base in elements(u.bookmarks)").setParameter("base", baseBean).getResultList();
				
				ArrayList<BookmarkService.ListenerUser> ret = new ArrayList<BookmarkService.ListenerUser>();
				for(Object[] row : results) {
					ListenerUser listener = new ListenerUser();
					listener.setId((Long)row[0]);
					listener.setName((String)row[1]);
					ret.add(listener);
				}
				return ret;
			}
		});
	}
	
}
