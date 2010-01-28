package org.todomap.spamegg;

import org.junit.Before;

public class AkismetSpamFilterTest extends SpamFilterTests {

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

	@Override
	SpamFilter getSpamFilter() {
		return spamFilter;
	}

}
