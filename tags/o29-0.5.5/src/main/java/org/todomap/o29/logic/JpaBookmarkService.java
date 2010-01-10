package org.todomap.o29.logic;

import java.util.ArrayList;
import java.util.List;

import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.Locatable;
import org.todomap.o29.beans.User;

public class JpaBookmarkService implements BookmarkService {

	UserService userService;
	BaseService baseService;
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void bookmark(final long itemId) {
		final User currentUser = userService.getCurrentUser();
		currentUser.getBookmarks().add(baseService.getById(itemId));
		userService.persist(currentUser);
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
	public void unbookmark(long itemId) {
		userService.getCurrentUser().getBookmarks().remove(baseService.getById(itemId));	
	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
	
}
