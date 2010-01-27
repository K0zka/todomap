package org.todomap.spamegg;

import org.junit.Before;
import org.junit.Test;

public class AkismetSpamFilterTest {

	SpamFilter spamFilter;

	@Before
	public void setup() {
		spamFilter = new AkismetSpamFilter();
		((AkismetSpamFilter) spamFilter).setApikey("73773ff76ecd");
		((AkismetSpamFilter) spamFilter)
				.setAppName("Spameggspam unit tests/0.1");
		((AkismetSpamFilter) spamFilter)
				.setFrontpage("http://code.google.com/p/todomap/");
	}

	@Test
	public void testIsSpam() {
		final Content spam = new Content(
				new User("John Doe","127.0.0.1", "spamboy@hotmail.com",
						"http://localhost/XXX", "Spambot 1.0"),
				"-",
				"",
				"comment",
				"Win the lottery, buy cheap viagra online 500% off now from the president of Nigeria.");
		spamFilter
				.isSpam(spam);
		spamFilter.falseNegative(spam);
	}
}
