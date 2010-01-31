package org.todomap.o29.utils;

import junit.framework.Assert;

import org.junit.Test;
import org.todomap.o29.utils.TodoFullPageServlet;

public class TodoFullPageServletTest {

	@Test
	public void testGet() {
		
	}
	
	@Test
	public void testExtractTodoId() {
		TodoFullPageServlet servlet = new TodoFullPageServlet();
		Assert.assertEquals(1, servlet.extractTodoId("1-bla-bla-bla.html"));
		Assert.assertEquals(2, servlet.extractTodoId("2-ize.txt"));
		Assert.assertEquals(0, servlet.extractTodoId("fubar"));
		Assert.assertEquals(1, servlet.extractTodoId("/fubar/1-hello.txt"));
		Assert.assertEquals(1, servlet.extractTodoId("fubar/1-hello.txt"));
	}
	
}
