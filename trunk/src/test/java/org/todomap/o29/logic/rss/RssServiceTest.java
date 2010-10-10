package org.todomap.o29.logic.rss;

import org.junit.Test;
import org.todomap.o29.beans.RssFeed;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

@SpringApplicationContext("org/todomap/o29/logic/rss/RssServiceTestCtx.xml")
public class RssServiceTest extends UnitilsJUnit4 {
	@SpringBeanByName
	RssService rssService;
	
	@Test
	public void testUpdateRssFeed() {
		RssFeed feed = new RssFeed();
		feed.setFeedUrl("http://chartporn.org/feed/");
		rssService.updateRssFeed(feed);
	}
	
}
