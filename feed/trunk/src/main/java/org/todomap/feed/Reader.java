package org.todomap.feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.todomap.feed.beans.Channel;
import org.todomap.feed.beans.Feed;
import org.todomap.feed.beans.NewsFeed;
import org.todomap.feed.beans.Rss;
import org.todomap.feed.utils.NewsFeedUtils;

public class Reader {
	static JAXBContext getContext() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(Rss.class
				.getPackage().getName());
		return context;
	}

	static Unmarshaller getUnmarshaler() throws JAXBException {
		final Unmarshaller unmarshaller = getContext().createUnmarshaller();
		return unmarshaller;
	}

	public static NewsFeed read(final InputStream stream) throws IOException {
		try {
			final Object obj = getUnmarshaler().unmarshal(stream);
			if (obj instanceof Feed) {
				return (Feed) obj;
			} else if (obj instanceof Rss) {
				return ((Rss) obj).getChannel();
			} else if (obj instanceof Channel) {
				return (Channel) obj;
			} else {
				throw new IOException("Broken feed: " + obj.getClass());
			}
		} catch (final JAXBException e) {
			throw new IOException(e);
		}
	}

	public static NewsFeed read(final String url) throws IOException {
		try (InputStream stream = new URL(url).openStream();) {
			NewsFeed feed = read(stream);
			if(feed != null && NewsFeedUtils.getSelfUrl(feed) == null) {
				NewsFeedUtils.setSelfLink(feed, url);
			}
			return feed;
		}
	}
}
