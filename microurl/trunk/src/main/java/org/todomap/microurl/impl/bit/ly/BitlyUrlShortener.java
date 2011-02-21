package org.todomap.microurl.impl.bit.ly;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.todomap.microurl.UrlShortener;
import org.todomap.microurl.UrlShortenerException;
import org.todomap.microurl.impl.BasicUrlShortener;

/**
 * http://code.google.com/p/bitly-api/wiki/ApiDocumentation#REST_API
 * 
 * @author kocka
 */
public class BitlyUrlShortener extends BasicUrlShortener implements
		UrlShortener {

	private String user;

	@Override
	public String shorten(String url) throws UrlShortenerException {
		try {
			URLConnection connection = new URL(
					"http://api.bitly.com/v3/shorten?format=txt&apiKey="
							+ getKey() + "&login=" + user + "&longUrl=" + url)
					.openConnection();
			return IOUtils.toString(connection.getInputStream());
		} catch (IOException e) {
			throw new UrlShortenerException(e);
		}
	}

	@Override
	public String expand(String url) throws UrlShortenerException {
		try {
			URLConnection connection = new URL(
					"http://api.bitly.com/v3/expand?format=txt&apiKey="
							+ getKey() + "&login=" + user + "&shortUrl=" + url)
					.openConnection();
			return IOUtils.toString(connection.getInputStream());
		} catch (IOException e) {
			throw new UrlShortenerException(e);
		}
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
