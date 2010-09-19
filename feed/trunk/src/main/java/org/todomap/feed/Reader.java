package org.todomap.feed;

import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.todomap.feed.beans.Rss;

public final class Reader {
	public Rss read(final String url) throws IOException {
		try {
			JAXBContext context = JAXBContext.newInstance(Rss.class.getPackage().getName());
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (Rss)unmarshaller.unmarshal(new URL(url).openStream());
		} catch (JAXBException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
}
