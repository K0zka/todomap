package org.todomap.feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

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
	private static DecompressingHttpClient getDefaultClient() {
		final DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.socket.timeout", 2000);
		client.getParams().setParameter("http.connection.timeout", 2000);
		return new DecompressingHttpClient(new AutoRetryHttpClient(
				new DecompressingHttpClient(client)));
	}

	public static NewsFeed read(final String url) throws IOException {
		final HttpClient client = getDefaultClient();
		return read(url, client, null);
	}

	public static NewsFeed read(final String url, final HttpClient client,
			final List<TransportCacheControl> clientCacheState)
			throws IOException {
		try {
			final HttpGet get = new HttpGet();
			get.setURI(new URI(url));
			if (clientCacheState != null) {
				for (final TransportCacheControl cacheHeader : clientCacheState) {
					get.setHeader(cacheHeader.requestHeaderName(),
							cacheHeader.getValue());
				}
			}
			final HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_MODIFIED) {
				return null;
			}

			final InputStream content = response.getEntity().getContent();
			final AbstractNewsFeed feed = (AbstractNewsFeed) Reader
					.read(content);

			setCacheControls(response, feed);
			setTransportCompression(content, feed);
			return feed;
		} catch (final IllegalStateException e) {
			throw new IOException(e);
		} catch (final URISyntaxException e) {
			throw new IOException(e);
		}
	}

	static void setTransportCompression(final InputStream content,
			final AbstractNewsFeed feed) {
		if (feed != null) {
			// this is a bit smelly, but looks like the only relyable way around
			// httpclient's compression
			feed.setTransportCompressed(content instanceof GZIPInputStream
					|| content instanceof DeflaterInputStream);
		}
	}

	public static NewsFeed read(final String url,
			final List<TransportCacheControl> clientCacheState)
			throws IOException {
		final HttpClient client = getDefaultClient();
		return read(url, client, clientCacheState);
	}

	static void setCacheControls(final HttpResponse response,
			final AbstractNewsFeed feed) {
		if (feed != null) {
			final ArrayList<TransportCacheControl> cacheControls = new ArrayList<>();
			for (final Header header : response.getAllHeaders()) {
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
		}
	}
}
