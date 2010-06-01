package org.todomap.spamegg;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * Akismet spam api based on <a
 * href="http://akismet.com/development/api/">aksimet api documentation</a>
 * 
 * @author kocka
 */
public class AkismetSpamFilter implements SpamFilter {

	private String serviceDomain = "rest.akismet.com";

	public String getServiceDomain() {
		return serviceDomain;
	}

	public void setServiceDomain(String serviceDomain) {
		this.serviceDomain = serviceDomain;
	}

	private static final String versionUrl = "$HeadURL: https://todomap.googlecode.com/svn/spameggspam/trunk/src/main $";

	private String apikey;
	private String appName;
	private String frontpage;

	public void falseNegative(final Content content) throws SpamFilterException {
		post("submit-spam", getRequest(content));
	}

	public void falsePositive(final Content content) throws SpamFilterException {
		post("submit-ham", getRequest(content));
	}

	public final String getApikey() {
		return apikey;
	}

	public final String getAppName() {
		return appName;
	}

	public final String getFrontpage() {
		return frontpage;
	}

	NameValuePair[] getRequest(final Content content) {
		return new NameValuePair[] {
				new NameValuePair("blog", frontpage),
				new NameValuePair("user_ip", content.getAuthor().getIpAddress()),
				new NameValuePair("user_agent", content.getAuthor()
						.getUserAgent()),
				new NameValuePair("comment_author_url", content.getAuthor()
						.getURL()),
				new NameValuePair("comment_author_email", content.getAuthor()
						.getEmail()),
				new NameValuePair("comment_author", content.getAuthor()
						.getName()),
				new NameValuePair("comment_content", content.getContent()) };
	}

	final String getVersion() {
		final int srcRootPos = versionUrl.indexOf("/src/", 0);
		final int lastSlash = versionUrl.lastIndexOf("/", srcRootPos - 1);
		return versionUrl.substring(lastSlash + 1, srcRootPos);
	}

	public boolean isSpam(final Content content) throws SpamFilterException {
		return Boolean.parseBoolean(post("comment-check", getRequest(content)));
	}

	private String post(final String operationName,
			final NameValuePair[] request) throws SpamFilterException {
		final HttpClient client = new HttpClient();
		final PostMethod post = new PostMethod();
		try {
			post.setURI(new URI("http://" + apikey + '.'+serviceDomain+"/1.1/"
					+ operationName, false));
			post.getParams().setParameter("http.useragent",
					appName + "| SpamEggSpam/" + getVersion());
			post.setRequestBody(request);
			client.executeMethod(post);
			return post.getResponseBodyAsString();
		} catch (IllegalArgumentException e) {
			throw new SpamFilterException(e.getMessage(), e);
		} catch (IOException e) {
			throw new SpamFilterException(e.getMessage(), e);
		}
	}

	public final void setApikey(final String apikey) {
		this.apikey = apikey;
	}

	public final void setAppName(final String appName) {
		this.appName = appName;
	}

	public final void setFrontpage(final String frontpage) {
		this.frontpage = frontpage;
	}

}
