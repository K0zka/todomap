package org.todomap.o29.logic;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.todomap.o29.beans.Todo;
import org.todomap.o29.beans.User;
import org.todomap.o29.logic.UserService;
import org.unitils.UnitilsJUnit4;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

@SpringApplicationContext("org/todomap/o29/logic/JpaUserServiceImplTestCtx.xml")
@DataSet
public class JpaUserServiceImplTest extends UnitilsJUnit4 {

	@SpringBean("userService")
	UserService userService;
	
	@Test
	public void testPersist() {
		User user = new User();
		user.setEmail(System.currentTimeMillis() + "@example.com");
		user.setDisplayName("TEST");
		user.setOpenIdUrl("http://"+System.currentTimeMillis()+".example.com/");
		userService.persist(user);
	}

	@Test
	public void testGetUserById() {
		User userById = userService.getUserById(-1);
		Assert.assertNotNull(userById);
	}

	@Test
	public void getUserByOpenId() {
		User user = userService.getUserByOpenIdUrl("http://bobek.example.com/");
		Assert.assertNotNull(user);
		Assert.assertEquals("http://bobek.example.com/", user.getOpenIdUrl());
	}

	@Test
	public void getUserByOpenId_notfound() {
		User user = userService.getUserByOpenIdUrl("http://notexisting.example.com/");
		Assert.assertNull(user);
	}

	@Test
	public void testListTodoBookmarks() {
		List<Todo> bookmarks = userService.listTodoBookmarks(userService.getUserByOpenIdUrl("http://bobek.example.com/"));
		Assert.assertNotNull(bookmarks);
	}
	
}
