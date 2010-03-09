package org.todomap.o29.logic;


import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.User;

public class JpaBaseService extends JpaDaoSupport implements BaseService{

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

}
