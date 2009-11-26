package org.todomap.o29.utils;

import junit.framework.Assert;

import org.junit.Test;

public class HtmlUtilTest {
	@Test
	public void testGetFirstParagraph() {
		Assert.assertEquals("lorem",  HtmlUtil.getFirstParagraph("<p>lorem</p><p>ipsum</p>"));
		Assert.assertEquals("lorem",  HtmlUtil.getFirstParagraph("lorem<br/>ipsum"));
		Assert.assertEquals("lorem",  HtmlUtil.getFirstParagraph("lorem<br>ipsum"));
	}
}
