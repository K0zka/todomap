package org.todomap.feed;

import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.todomap.feed.beans.Feed;
import org.todomap.feed.beans.NewsFeed;
import org.todomap.feed.beans.Rss;

public final class Reader {
	public NewsFeed read(final String url) throws IOException {
		try {
			JAXBContext context = JAXBContext.newInstance(Rss.class.getPackage().getName());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Object obj = unmarshaller.unmarshal(new URL(url).openStream());
			if(obj instanceof Feed) {
				return (Feed)obj;
			} else if(obj instanceof Rss){
				return ((Rss)obj).getChannel();
			} else {
				throw new IOException("Brokent feed: "+obj.getClass());
			}
		} catch (JAXBException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
}
