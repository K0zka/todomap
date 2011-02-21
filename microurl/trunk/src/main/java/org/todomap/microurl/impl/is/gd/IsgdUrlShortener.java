package org.todomap.microurl.impl.is.gd;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.todomap.microurl.ManageableUrlShortener;
import org.todomap.microurl.UrlShortenerException;

/**
 * http://is.gd/apilookupreference.php
 * 
 * @author kocka
 */
public class IsgdUrlShortener implements ManageableUrlShortener {

	@Override
	public String shorten(String url) throws UrlShortenerException {
		try {
			URLConnection connection = new URL(
					"http://is.gd/create.php?format=simple&logstats=1&url=" + url)
					.openConnection();
			return IOUtils.toString(connection.getInputStream());
		} catch (IOException e) {
			throw new UrlShortenerException(e);
		}
	}

	@Override
	public String expand(final String url) throws UrlShortenerException {
		try {
			final URLConnection connection = new URL(
					"http://is.gd/forward.php?format=simple&shorturl=" + url)
					.openConnection();
			return IOUtils.toString(connection.getInputStream());
		} catch (IOException e) {
			throw new UrlShortenerException(e);
		}
	}

	@Override
	public long getClicks(String url) {
//		final URLConnection connection = new URL("http://is.gd/stats.php?url=").openConnection();
		
		return 0;
	}

}
