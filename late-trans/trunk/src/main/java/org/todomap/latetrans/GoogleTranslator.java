package org.todomap.latetrans;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GoogleTranslator implements Translator {

	private String key = "notsupplied";
	
	public String getLanguage(final String text) {
		final HttpClient client = new HttpClient();
		final GetMethod method = new GetMethod("http://www.google.com/uds/GlangDetect");
		final NameValuePair[] nameValuePairs = new NameValuePair[]{
				new NameValuePair("q", text),
				new NameValuePair("key", key),
				new NameValuePair("v","1.0")
		};
		method.setQueryString(nameValuePairs);
		try {
			client.executeMethod(method);
			final JSONObject object = new JSONObject(method.getResponseBodyAsString());
			return object.getJSONObject("responseData").getString("language");
		} catch (HttpException e) {
			return null;
		} catch (IOException e) {
			return null;
		} catch (JSONException e) {
			return null;
		}
	}

}
