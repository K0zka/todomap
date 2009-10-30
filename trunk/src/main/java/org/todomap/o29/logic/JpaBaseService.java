package org.todomap.o29.logic;


import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.BaseBean;

public class JpaBaseService extends JpaDaoSupport implements BaseService{

	@Override
	public BaseBean getById(final long id) {
		return getJpaTemplate().find(BaseBean.class, id);
	}

}
