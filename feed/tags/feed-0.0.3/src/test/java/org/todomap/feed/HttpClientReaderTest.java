package org.todomap.feed;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.todomap.feed.beans.NewsFeed;

public class HttpClientReaderTest {
	@Test
	public void testRead() throws IOException {
		NewsFeed feed = HttpClientReader.read("http://iwillworkforfood.blogspot.com/feeds/posts/default");
		Assert.assertNotNull(feed);
	}

	@Test
	public void testReread() throws IOException {
		NewsFeed feed = HttpClientReader.read("http://iwillworkforfood.blogspot.com/feeds/posts/default");
		Assert.assertNotNull(feed);
		NewsFeed reReadFeed = HttpClientReader.read("http://iwillworkforfood.blogspot.com/feeds/posts/default", feed.getCacheControl());
		Assert.assertNull(reReadFeed);
	}

}
