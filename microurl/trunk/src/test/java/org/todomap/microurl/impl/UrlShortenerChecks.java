package org.todomap.microurl.impl;

import junit.framework.Assert;

import org.junit.Test;
import org.todomap.microurl.UrlShortener;
import org.todomap.microurl.UrlShortenerException;

public abstract class UrlShortenerChecks {
	public abstract UrlShortener getUrlShortener();

	@Test
	public void testShorten() throws UrlShortenerException {
		final UrlShortener shortener = getUrlShortener();
		final String shortUrl = shortener.shorten("http://www.google.com/");
		Assert.assertNotNull(shortUrl);
		Assert.assertTrue(shortUrl.startsWith("http"));
	}

	@Test
	public void testExpand() throws UrlShortenerException {
		final UrlShortener shortener = getUrlShortener();
		String expand = shortener.expand(shortener
				.shorten("http://www.google.com/"));
		// at least in case of bit.ly, it will not be equal to the original url,
		// but at least similar
		Assert.assertTrue(expand.startsWith("http://www.google.com"));
	}

}
