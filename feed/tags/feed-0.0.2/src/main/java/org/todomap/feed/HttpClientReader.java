package org.todomap.feed;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AutoRetryHttpClient;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.todomap.feed.beans.NewsFeed;

public class HttpClientReader {
	public static NewsFeed read(final String url) throws IOException {
		HttpClient client = new DecompressingHttpClient(
				new AutoRetryHttpClient(new DefaultHttpClient()));
		return read(url, client);
	}

	public static NewsFeed read(final String url, HttpClient client)
			throws IOException {
		try {
			HttpGet get = new HttpGet();
			get.setURI(new URI(url));
			HttpResponse response = client.execute(get);
			return Reader.read(response.getEntity().getContent());
		} catch (IllegalStateException e) {
			throw new IOException(e);
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}
}
