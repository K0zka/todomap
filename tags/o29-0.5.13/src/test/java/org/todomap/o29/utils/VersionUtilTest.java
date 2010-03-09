package org.todomap.o29.utils;

import org.junit.Assert;
import org.junit.Test;
import org.todomap.o29.utils.VersionUtil;

public class VersionUtilTest {
	@Test
	public void testGetVersionNumber() {
		String versionNumber = VersionUtil.getVersionNumber();
		Assert.assertNotNull(versionNumber);
		Assert.assertFalse(versionNumber.contains("/"));
	}

}
