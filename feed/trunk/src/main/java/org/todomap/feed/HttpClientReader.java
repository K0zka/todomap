package org.todomap.feed;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AutoRetryHttpClient;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.todomap.feed.beans.AbstractNewsFeed;
import org.todomap.feed.beans.NewsFeed;
import org.todomap.feed.beans.transport.EtagCacheControl;
import org.todomap.feed.beans.transport.LastModifiedCacheControl;
import org.todomap.feed.beans.transport.TransportCacheControl;

public class HttpClientReader {
	public static NewsFeed read(final String url) throws IOException {
		final HttpClient client = new DecompressingHttpClient(
				new AutoRetryHttpClient(new DecompressingHttpClient(
						new DefaultHttpClient())));
		return read(url, client, null);
	}

	public static NewsFeed read(final String url,
			List<TransportCacheControl> clientCacheState) throws IOException {
		final HttpClient client = new DecompressingHttpClient(
				new AutoRetryHttpClient(new DecompressingHttpClient(
						new DefaultHttpClient())));
		return read(url, client, clientCacheState);
	}

	public static NewsFeed read(final String url, HttpClient client,
			List<TransportCacheControl> clientCacheState) throws IOException {
		try {
			HttpGet get = new HttpGet();
			get.setURI(new URI(url));
			if (clientCacheState != null) {
				for (TransportCacheControl cacheHeader : clientCacheState) {
					get.setHeader(cacheHeader.requestHeaderName(),
							cacheHeader.getValue());
				}
			}
			HttpResponse response = client.execute(get);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_MODIFIED) {
				return null;
			} 

			AbstractNewsFeed feed = (AbstractNewsFeed) Reader.read(response
					.getEntity().getContent());

			ArrayList<TransportCacheControl> cacheControls = new ArrayList<>();
			for (Header header : response.getAllHeaders()) {
				switch (header.getName()) {
				case "ETag":
					cacheControls.add(new EtagCacheControl(header.getValue()));
					break;
				case "Last-Modified":
					cacheControls.add(new LastModifiedCacheControl(header
							.getValue()));
					break;
				}
			}

			feed.setCacheControl(cacheControls);
			return feed;
		} catch (IllegalStateException e) {
			throw new IOException(e);
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}
}
