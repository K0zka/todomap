package net.anzix.o29.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.support.JpaDaoSupport;

public class JpaHomeServiceImpl extends JpaDaoSupport implements HomeService {

	private final static Logger logger = LoggerFactory.getLogger(JpaHomeServiceImpl.class);
	
	@Override
	public HomeLocation getHome() {
		return new HomeLocation();
	}

	@Override
	public void setHome(HomeLocation loc) {
		logger.info("Update loc");
	}

}
