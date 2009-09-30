package net.anzix.o29.utils;

import org.junit.Assert;
import org.junit.Test;

public class VersionUtilTest {
	@Test
	public void getVersionNumber() {
		String versionNumber = VersionUtil.getVersionNumber();
		Assert.assertNotNull(versionNumber);
		Assert.assertFalse(versionNumber.contains("/"));
	}
}
