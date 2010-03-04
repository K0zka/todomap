package org.todomap.o29.utils;

import junit.framework.Assert;

import org.junit.Test;

public class HtmlUtilTest {

	@Test
	public void testTextify() {
		Assert.assertEquals(HtmlUtil.textify("<b>bla</b>bla"), "blabla");
		Assert.assertEquals(HtmlUtil.textify("bla<br>bla"), "blabla");
	}

	@Test
	public void testCleanup() {
		Assert
				.assertEquals(
						"hello  world!",
						HtmlUtil
								.cleanup("hello <script language='javascript'>\nalert('wrong!')</script> world!"));
		Assert.assertEquals("hello  world!", HtmlUtil
				.cleanup("hello <embed/> world!"));
		Assert.assertEquals("hello  world!", HtmlUtil
				.cleanup("hello <!--you cruel--> world!"));
	}

	@Test
	public void testGetFirstParagraph() {
		Assert.assertEquals("lorem", HtmlUtil
				.getFirstParagraph("<p>lorem</p><p>ipsum</p>"));
		Assert.assertEquals("lorem", HtmlUtil
				.getFirstParagraph("lorem<br/>ipsum"));
		Assert.assertEquals("lorem", HtmlUtil
				.getFirstParagraph("lorem<br>ipsum"));
		Assert.assertEquals("lorem", HtmlUtil
				.getFirstParagraph("<div>lorem<br>ipsum</div>"));
		Assert.assertEquals("lorem", HtmlUtil
				.getFirstParagraph("<span>lorem<br>ipsum</span>"));
		Assert.assertEquals("lorem", HtmlUtil
				.getFirstParagraph("<span>lorem&nbsp;ipsum</span>"));
	}

	@Test
	public void testIsAccepted() {
		Assert.assertTrue(HtmlUtil.isAcceptedTag("div"));
		Assert.assertTrue(HtmlUtil.isAcceptedTag("DIV"));
		Assert.assertFalse(HtmlUtil.isAcceptedTag("script"));
		Assert.assertFalse(HtmlUtil.isAcceptedTag("SCRIPT"));
		Assert.assertFalse(HtmlUtil.isAcceptedTag("EMBED"));
	}
}
