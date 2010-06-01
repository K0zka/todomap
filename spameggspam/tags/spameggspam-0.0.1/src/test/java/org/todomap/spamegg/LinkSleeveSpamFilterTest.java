package org.todomap.spamegg;


public class LinkSleeveSpamFilterTest extends SpamFilterTests {

	@Override
	SpamFilter getSpamFilter() {
		return new LinkSleeveSpamFilter();
	}

}
