package org.todomap.spamegg;

import java.util.HashMap;

public class ScriptedSpamFilterTest extends SpamFilterTests {

	@Override
	SpamFilter getSpamFilter() {
		HashMap<String, SpamFilter> backends = new HashMap<String, SpamFilter>();
		AkismetSpamFilter spamFilter = new AkismetSpamFilter();
		((AkismetSpamFilter) spamFilter).setApikey("73773ff76ecd");
		((AkismetSpamFilter) spamFilter)
				.setAppName("Spameggspam unit tests/0.1");
		((AkismetSpamFilter) spamFilter)
				.setFrontpage("http://code.google.com/p/todomap/");

		AkismetSpamFilter typepadSpamfilter = new AkismetSpamFilter();
		typepadSpamfilter.setApikey("406d196bc29d4a8edf274a5165da1023");
		typepadSpamfilter.setServiceDomain("api.antispam.typepad.com");

		backends.put("akismet", spamFilter);
		backends.put("linksleeve", new LinkSleeveSpamFilter());
		backends.put("typepad", typepadSpamfilter);

		return new ScriptedSpamFilter(
				backends,
				" linksleeve.isSpam(content) || typepad.isSpam(content)|| akismet.isSpam(content)",
				"javascript");
	}
}
