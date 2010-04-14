package org.todomap.o29.utils.security;

import junit.framework.Assert;

import org.junit.Test;

public class SecurityFilterTest {
	@Test
	public void testIsProtected() {
		Assert.assertTrue(SecurityFilter.isProtectedMethod("POST", "/services/bookmark/add"));
		Assert.assertTrue(SecurityFilter.isProtectedMethod("PUT", "/services/bookmark/add"));
		Assert.assertFalse(SecurityFilter.isProtectedMethod("POST", "/services/anon/rate/add/1/2"));
	}
}
