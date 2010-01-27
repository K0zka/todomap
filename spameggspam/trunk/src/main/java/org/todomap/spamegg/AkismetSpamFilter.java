package org.todomap.spamegg;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.PostMethod;

public class AkismetSpamFilter implements SpamFilter {

	private static final String versionUrl = "$HeadURL: https://todomap.googlecode.com/svn/spameggspam/trunk/src/main $";

	final String getVersion() {
		final int srcRootPos = versionUrl.indexOf("/src/", 0);
		final int lastSlash = versionUrl.lastIndexOf("/", srcRootPos - 1);
		return versionUrl.substring(lastSlash + 1, srcRootPos);
	}

	String apikey;
	String frontpage;
	String appName;

	public void falseNegative(Content content) {
		post("submit-spam", getRequest(content));
	}

	NameValuePair[] getRequest(final Content content) {
		return new NameValuePair[] {
				new NameValuePair("blog", frontpage),
				new NameValuePair("user_ip", content.getAuthor().ipAddress),
				new NameValuePair("user_agent", content.getAuthor().userAgent),
				new NameValuePair("comment_author_url", content.getAuthor()
						.getURL()),
				new NameValuePair("comment_author", content.getAuthor()
								.getName()),
				new NameValuePair("comment_content", content.getContent()),
				new NameValuePair()};
	}

	public void falsePositive(Content content) {
		post("submit-ham", getRequest(content));
	}

	public boolean isSpam(Content content) {
		return Boolean.parseBoolean(post("comment-check", getRequest(content)));
	}

	private String post(final String operationName,
			final NameValuePair[] request) {
		final HttpClient client = new HttpClient();
		final PostMethod post = new PostMethod();
		try {
			post.setURI(new URI("http://" + apikey + ".rest.akismet.com/1.1/"
					+ operationName, false));
			post.getParams().setParameter("http.useragent",
					appName + "| SpamEggSpam/" + getVersion());
			post.setRequestBody(request);
			client.executeMethod(post);
			return post.getResponseBodyAsString();
		} catch (URIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getFrontpage() {
		return frontpage;
	}

	public void setFrontpage(String frontpage) {
		this.frontpage = frontpage;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}
