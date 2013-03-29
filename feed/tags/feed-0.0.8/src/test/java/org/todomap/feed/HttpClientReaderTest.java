package org.todomap.feed;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.junit.Test;
import org.mockito.Mockito;
import org.todomap.feed.beans.NewsFeed;
import org.todomap.feed.utils.NewsFeedUtils;

public class HttpClientReaderTest {
	@Test
	public void testRead() throws IOException {
		NewsFeed feed = HttpClientReader.read("http://iwillworkforfood.blogspot.com/feeds/posts/default");
		Assert.assertNotNull(feed);
	}

	@Test
	public void testReadNoLink() throws IOException {
		NewsFeed feed = HttpClientReader.read("http://pulispace.com/en/media/blog?format=feed&type=rss");
		Assert.assertNotNull(feed);
		Assert.assertNotNull(feed.getLinks());
		Assert.assertNotNull(NewsFeedUtils.getSelfUrl(feed));
	}
	
	@Test
	public void testReadWithGzip() throws IOException {
		NewsFeed feed = HttpClientReader.read("http://blog.dictat.org/?feed=rss2");
		Assert.assertNotNull(feed);
		Assert.assertTrue(feed.isTransportCompressed());
	}

	@Test
	public void testReread() throws IOException {
		NewsFeed feed = HttpClientReader.read("http://iwillworkforfood.blogspot.com/feeds/posts/default");
		Assert.assertNotNull(feed);
		NewsFeed reReadFeed = HttpClientReader.read("http://iwillworkforfood.blogspot.com/feeds/posts/default", feed.getCacheControl());
		Assert.assertNull(reReadFeed);
	}
	@Test
	public void testSetCacheControls() {
		HttpClientReader.setCacheControls(Mockito.mock(HttpResponse.class), null);
		//TODO: test with non null feed
	}

	@Test
	public void testSetTransportCompression() {
		HttpClientReader.setTransportCompression(Mockito.mock(InputStream.class), null);
		//TODO: test witj non null feed
	}
}
