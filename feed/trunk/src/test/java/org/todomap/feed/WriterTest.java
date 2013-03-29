package org.todomap.feed;

import java.io.IOException;

import org.junit.Test;
import org.todomap.feed.beans.NewsFeed;

public class WriterTest {
	@Test
	public void testWriteAtom() throws IOException {
		NewsFeed atom = HttpClientReader.read("http://iwillworkforfood.blogspot.com/feeds/posts/default?alt=atom");
		Writer.write(atom, System.out);
	}
	@Test
	public void testWriteRss() throws IOException {
		NewsFeed rss = HttpClientReader.read("http://iwillworkforfood.blogspot.com/feeds/posts/default?alt=rss");
		Writer.write(rss, System.out);
	}
}
