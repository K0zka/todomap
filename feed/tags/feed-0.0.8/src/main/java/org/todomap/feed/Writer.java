package org.todomap.feed;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.todomap.feed.beans.Channel;
import org.todomap.feed.beans.NewsFeed;
import org.todomap.feed.beans.Rss;

public class Writer {

	static Marshaller getMarshaler() throws JAXBException {
		return Reader.getContext().createMarshaller();
	}

	public static void write(final NewsFeed feed, final OutputStream out)
			throws IOException {
		try {
			if (feed instanceof Channel) {
				Rss rss = new Rss();
				rss.setChannel(((Channel) feed));
				getMarshaler().marshal(rss, out);
			} else {
				getMarshaler().marshal(feed, out);
			}
		} catch (final JAXBException e) {
			throw new IOException(e);
		}
	}

}
