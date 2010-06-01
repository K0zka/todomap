package org.todomap.spamegg;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * A simple and lightwight <a href="http://www.linksleeve.org/#postmethod">LinkSleeve client</a>.
 * 
 * @author kocka
 *
 */
public class LinkSleeveSpamFilter implements SpamFilter {

	@Override
	public void falseNegative(final Content content) throws SpamFilterException {
		// not implemented by linksleeve
	}

	@Override
	public void falsePositive(final Content content) throws SpamFilterException {
		// not implemented by linksleeve
	}

	@Override
	public boolean isSpam(final Content content) throws SpamFilterException {
		final HttpClient client = new HttpClient();
		final PostMethod method = new PostMethod(
				"http://www.linksleeve.org/pslv.php");
		client.getParams().setParameter("content", content.getContent());
		try {
			client.executeMethod(method);
			final String response = method.getResponseBodyAsString();
			return "-slv-1-/slv-".equals(response.trim());
		} catch (final HttpException e) {
			throw new SpamFilterException(e.getMessage(), e);
		} catch (final IOException e) {
			throw new SpamFilterException(e.getMessage(), e);
		}
	}

}
