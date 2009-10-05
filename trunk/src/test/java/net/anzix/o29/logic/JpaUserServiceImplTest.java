package net.anzix.o29.logic;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;
import net.anzix.o29.beans.User;

@SpringApplicationContext("net/anzix/o29/logic/JpaUserServiceImplTestCtx.xml")
public class JpaUserServiceImplTest extends UnitilsJUnit4 {

	@SpringBean("userService")
	UserService userService;
	
	@Test
	public void testAddUser() {
		User user = new User();
		user.setEmail(System.currentTimeMillis() + "@example.com");
		user.setDisplayName("TEST");
		userService.addUser(user);
	}

	@Test
	public void testGetUserById() {
//		userService.getUserById(1);
	}

}
