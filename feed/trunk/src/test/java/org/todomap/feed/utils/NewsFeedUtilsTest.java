package org.todomap.feed.utils;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;
import org.todomap.feed.beans.Channel;
import org.todomap.feed.beans.Link;

public class NewsFeedUtilsTest {
	@Test
	public void testGetPubSubHub() {
		Channel feed = new Channel();
		Assert.assertNull(NewsFeedUtils.getPubSubHub(feed));
		feed.setLinks(null);
		Assert.assertNull(NewsFeedUtils.getPubSubHub(feed));
		feed.setLinks(new ArrayList<Link>());
		feed.getLinks().add(new Link("hub",null,"http://example.com",null));
		Assert.assertEquals("http://example.com",NewsFeedUtils.getPubSubHub(feed));
	}
}
