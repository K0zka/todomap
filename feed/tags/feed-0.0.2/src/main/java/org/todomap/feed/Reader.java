package org.todomap.feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.todomap.feed.beans.Feed;
import org.todomap.feed.beans.NewsFeed;
import org.todomap.feed.beans.Rss;

public class Reader {
	public static NewsFeed read(final String url) throws IOException {
			try (InputStream stream = new URL(url).openStream();) {
				return read(stream);
			}
	}

	public static NewsFeed read(final InputStream stream) throws IOException {
		try {
			final JAXBContext context = JAXBContext.newInstance(Rss.class
					.getPackage().getName());
			final Unmarshaller unmarshaller = context.createUnmarshaller();
			final Object obj = unmarshaller.unmarshal(stream);
			if (obj instanceof Feed) {
				return (Feed) obj;
			} else if (obj instanceof Rss) {
				return ((Rss) obj).getChannel();
			} else {
				throw new IOException("Brokent feed: " + obj.getClass());
			}
		} catch (JAXBException e) {
			throw new IOException(e);
		}
	}
}
