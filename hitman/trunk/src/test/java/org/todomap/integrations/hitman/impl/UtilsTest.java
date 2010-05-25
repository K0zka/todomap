package org.todomap.integrations.hitman.impl;

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.dom.Document;

public class UtilsTest {
	@Test
	public void testParse() {
		Utils.parse("<bla> <!--foo--> <bar/> </bla>");
	}

	@Test(expected=RuntimeException.class)
	public void testParse_wrong() {
		Utils.parse("broken on purpose");
	}

	@Test
	public void testGetXpathValue() {
		Document doc = Utils.parse("<foo><bar>aprocska kalapocska benne csacska macska mocska</bar></foo>");
		Assert.assertEquals("aprocska kalapocska benne csacska macska mocska", Utils.getXpathValue("/foo/bar/text()", doc));
	}
}
