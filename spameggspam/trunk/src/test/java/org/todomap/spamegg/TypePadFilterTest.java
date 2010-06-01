package org.todomap.spamegg;

public class TypePadFilterTest extends SpamFilterTests {

	@Override
	SpamFilter getSpamFilter() {
		AkismetSpamFilter akismetSpamFilter = new AkismetSpamFilter();
		akismetSpamFilter.setApikey("406d196bc29d4a8edf274a5165da1023");
		akismetSpamFilter.setServiceDomain("api.antispam.typepad.com");
		return akismetSpamFilter;
	}

}
