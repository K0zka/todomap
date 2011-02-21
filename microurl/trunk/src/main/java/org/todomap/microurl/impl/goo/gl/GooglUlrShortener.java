package org.todomap.microurl.impl.goo.gl;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.todomap.microurl.ManageableUrlShortener;
import org.todomap.microurl.UrlShortener;
import org.todomap.microurl.UrlShortenerException;
import org.todomap.microurl.impl.BasicUrlShortener;

/**
 * {@link UrlShortener} implementation with Google's URL shortener service
 * available at http://goo.gl/.
 * 
 * @author kocka
 */
public class GooglUlrShortener extends BasicUrlShortener implements
		ManageableUrlShortener {

	final HttpClient client = new DefaultHttpClient();

	public String shorten(String url) throws UrlShortenerException {
		try {
			final HttpPost request = new HttpPost(getUrl());
			request.setHeader("Content-Type", "application/json");
			request.setEntity(new StringEntity("{\"longUrl\": \"" + url + "\"}"));
			final HttpResponse response = client.execute(request);
			final JSONObject jsonObject = new JSONObject(IOUtils.toString(response
					.getEntity().getContent()));
			return jsonObject.getString("id");
		} catch (IOException e) {
			throw new UrlShortenerException(e);
		} catch (IllegalStateException e) {
			throw new UrlShortenerException(e);
		} catch (JSONException e) {
			throw new UrlShortenerException(e);
		}
	}

	private String getUrl() {
		return "https://www.googleapis.com/urlshortener/v1/url"
				+ (getKey() == null ? "" : "?key=" + getKey());
	}

	public String expand(String url) throws UrlShortenerException {
		try {
			final HttpGet get = new HttpGet(getUrl() + (getKey() == null ? "?" : "&") + "shortUrl="+url);
			final HttpResponse response = client.execute(get);
			final JSONObject jsonObject = new JSONObject(IOUtils.toString(response
					.getEntity().getContent()));
			return jsonObject.getString("longUrl");
		} catch (IllegalStateException e) {
			throw new UrlShortenerException(e);
		} catch (IOException e) {
			throw new UrlShortenerException(e);
		} catch (JSONException e) {
			throw new UrlShortenerException(e);
		}
	}

	public long getClicks(String url) {
		return 0;
	}

}
