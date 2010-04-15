package org.todomap.o29.utils;

import junit.framework.Assert;

import org.junit.Test;

public class URLUtilTest {

	@Test
	public void testGet() {
		
	}
	
	@Test
	public void testExtractTodoId() {
		Assert.assertEquals(1, URLUtil.extractId("1-bla-bla-bla.html"));
		Assert.assertEquals(2, URLUtil.extractId("2-ize.txt"));
		Assert.assertEquals(0, URLUtil.extractId("fubar"));
		Assert.assertEquals(1, URLUtil.extractId("/fubar/1-hello.txt"));
		Assert.assertEquals(1, URLUtil.extractId("fubar/1-hello.txt"));
	}
	
}
