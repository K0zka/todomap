package org.todomap.feed;

import java.io.IOException;
import java.io.InputStream;

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
		check("http://iwillworkforfood.blogspot.com/feeds/posts/default?alt=atom");
	}

	@Test(expected = IOException.class)
	public void testFail() throws IOException {
		check("http://intenitonallynotexistingdomain.dictat.org");
	}

	@Test
	public void testRSS() throws IOException {
		check("http://planet.postgresql.org/rss20.xml");
		check("http://pulispace.com/en/media/blog?format=feed&type=rss");
		// check("https://twitter.com/statuses/user_timeline/kozka.rss");
		check("http://iwillworkforfood.blogspot.com/feeds/posts/default?alt=rss");
		check("http://tifyty.wordpress.com/feed/");
	}

	@Test
	public void testStupidDateFormat() throws IOException {
		checkTestResource("stupiddateformat-rss.xml");
	}

	@Test
	public void testAtomResource() throws IOException {
		checkTestResource("iwillworkforfood-atom.xml");
	}

	@Test
	public void testRssResource() throws IOException {
		checkTestResource("iwillworkforfood-rss.xml");
	}

	private void checkTestResource(String input) throws IOException {
		try (InputStream stream = Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(input)) {
			NewsFeed feed = Reader.read(stream);
			check(feed);
		}
	}

	private void check(String url) throws IOException {
		NewsFeed feed = Reader.read(url);
		check(feed);
	}

	private void check(NewsFeed feed) {
		Assert.assertNotNull(feed);
		for (NewsItem item : feed.getNewsItems()) {
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

	@Test
	public void emptyRss() throws IOException {
		NewsFeed feed = Reader.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("emptyrss.xml"));
		Assert.assertNull(feed);
	}
	
}
