package org.todomap.spamegg;

import org.junit.Test;

public abstract class SpamFilterTests {
	abstract SpamFilter getSpamFilter();

	@Test
	public void testIsSpam() throws SpamFilterException {
		final SpamFilter spamFilter = getSpamFilter();
		final Content spam = new Content(
				new User("John Doe", "127.0.0.1", "spamboy@hotmail.com",
						"http://localhost/XXX", "Spambot 1.0"),
				"-",
				"",
				"comment",
				"Win the lottery, buy cheap viagra online 500% off now from the president of Nigeria.");
		spamFilter.isSpam(spam);
		spamFilter.falseNegative(spam);
	}

	@Test
	public void testFalsePositive() throws SpamFilterException {
		final SpamFilter spamFilter = getSpamFilter();
		final Content notSpam = new Content(new User("Dummy Warhead",
				"127.0.0.1", "dummywarhead@hotmail.com",
				"http://dummywarhead.openid.net/", "Firefox"), "-", "",
				"comment", "Hello! This is a test.");
		spamFilter.isSpam(notSpam);
		spamFilter.falsePositive(notSpam);
	}

}
