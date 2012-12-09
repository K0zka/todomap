package org.todomap.feed.utils;

import junit.framework.Assert;

import org.junit.Test;

public class StringUtilsTest {
	@Test
	public void testMax() {
		Assert.assertEquals("i...", StringUtils.max("iwillworkforfood", 4));
		Assert.assertEquals("iwillworkforfood", StringUtils.max("iwillworkforfood", 32));
		Assert.assertEquals("iwill", StringUtils.max("iwill", 5));
		Assert.assertEquals("i~", StringUtils.max("iwill", 2));
		Assert.assertNull(StringUtils.max(null, 2));
	}
}
