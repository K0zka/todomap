package org.todomap.feed.utils;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;
import org.todomap.feed.Reader;
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
	@Test
	public void testGetPubSubHub_iwillworkforfood() throws IOException {
		//as much as we can rely on google
		Assert.assertNotNull(Reader.read("http://iwillworkforfood.blogspot.com/feeds/posts/default?alt=atom"));
	}
}
