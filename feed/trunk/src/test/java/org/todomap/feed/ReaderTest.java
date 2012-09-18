package org.todomap.feed;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import junit.framework.Assert;

import org.junit.Test;
import org.todomap.feed.beans.Channel;
import org.todomap.feed.beans.NewsFeed;
import org.todomap.feed.beans.NewsItem;
import org.todomap.feed.beans.Rss;

public class ReaderTest {

	@Test
	public void testAtom() throws IOException {
		check("http://iwillworkforfood.blogspot.com/feeds/posts/default");
	}

	@Test
	public void testRSS() throws IOException {
		check("http://planet.postgresql.org/rss20.xml");
		check("http://pulispace.com/en/media/blog?format=feed&type=rss");
		check("http://iwillworkforfood.blogspot.com/feeds/posts/default?alt=rss");
	}

	private void check(String url) throws IOException {
		Reader reader = new Reader();
		NewsFeed feed = reader.read(url);
		Assert.assertNotNull(feed);
		for(NewsItem item : feed.getNewsItems()) {
			Assert.assertNotNull(item);
			Assert.assertNotNull(item.getTitle());
		}
	}

	@Test
	public void serialize() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Rss.class.getPackage()
				.getName());
		Marshaller marshaller = context.createMarshaller();
		Rss rss = new Rss();
		Channel channel = new Channel();
		channel.setDescription("just a test");
		channel.setCategory("test");
		channel.setTitle("rss serialization test");
		marshaller.marshal(rss, System.out);

	}

}
