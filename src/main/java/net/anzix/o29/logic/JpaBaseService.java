package net.anzix.o29.logic;

import net.anzix.o29.beans.BaseBean;

import org.springframework.orm.jpa.support.JpaDaoSupport;

public class JpaBaseService extends JpaDaoSupport implements BaseService{

	@Override
	public BaseBean getById(final long id) {
		return getJpaTemplate().find(BaseBean.class, id);
	}

}
