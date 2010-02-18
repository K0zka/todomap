package org.todomap.hu.mohu;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;

public class Mohu {

	List<Contact> listContacts(String postalCode, String town)
			throws MalformedURLException, IOException {
		final ArrayList<Contact> ret = new ArrayList<Contact>();
		final HtmlCleaner cleaner = new HtmlCleaner();
		TagNode results = getFindNodeByClass(cleaner.clean(makeRequest(
				postalCode, town)), "div", "resultset resultset-last");
		if (results == null) {
			results = getFindNodeByClass(cleaner.clean(makeRequestToPostCity(
					postalCode, town)), "div", "resultset resultset-last");
		}

		StringWriter stringWriter = new StringWriter();
		results.serialize(new PrettyXmlSerializer(cleaner.getProperties()),
				stringWriter);
		System.out.println(stringWriter.toString());

		return ret;
	}

	private String makeRequestToPostCity(final String postalCode,
			final String town) throws IOException, MalformedURLException,
			UnsupportedEncodingException {

		final HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams
				.setParameter(
						"http.useragent",
						"Mozilla/5.0 (X11; U; Linux x86_64; en-US) AppleWebKit/532.9 (KHTML, like Gecko) Chrome/5.0.307.7 Safari/532.9");
		httpClientParams.setContentCharset("UTF-8");
		
		final HttpClient client = new HttpClient(httpClientParams);
		
		makeSession(client);

		final PostMethod postMethod = new PostMethod(
				"http://www.magyarorszag.hu:80/kozigazgatas/intezmenyek/onkig/testonk/jegyzo/polghiv/pf/searchofficeinpage/submitCity");
		postMethod.setParameter(
				"searchofficeinpage_2{actionForm.typedSettlement}", town);
		postMethod.setParameter(
				"searchofficeinpage_2{actionForm.typedPostCode}",
				postalCode == null ? "" : postalCode);
		postMethod
				.setParameter(
						"searchofficeinpage_2wlw-select_key:{actionForm.selectedSettlementPostCode}",
						"true");
		postMethod.setParameter("x", "8");
		postMethod.setParameter("y", "8");
		client.executeMethod(postMethod);
		return postMethod.getResponseBodyAsString();
	}

	private void makeSession(final HttpClient client) throws IOException,
			HttpException {
		client.executeMethod(new GetMethod("http://www.magyarorszag.hu:80/kozigazgatas/intezmenyek/onkig/testonk/jegyzo/polghiv/"));
	}

	@SuppressWarnings("unchecked")
	private TagNode getFindNodeByClass(final TagNode node, final String name,
			final String cssClass) {
		final List<TagNode> children = node.getAllElementsList(true);
		for (final TagNode child : children) {
			if (name.equals(((TagNode) child).getName())
					&& cssClass.equals(((TagNode) child)
							.getAttributeByName("class"))) {
				return (TagNode) child;
			} else {
				final TagNode grandchild = getFindNodeByClass((TagNode) child,
						name, cssClass);
				if (grandchild != null) {
					return grandchild;
				}
			}
		}
		return null;
	}

	private String makeRequest(final String postalCode, final String town)
			throws IOException, MalformedURLException,
			UnsupportedEncodingException {

		final HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams
				.setParameter(
						"http.useragent",
						"Mozilla/5.0 (X11; U; Linux x86_64; en-US) AppleWebKit/532.9 (KHTML, like Gecko) Chrome/5.0.307.7 Safari/532.9");
		httpClientParams.setContentCharset("UTF-8");
		final HttpClient client = new HttpClient(httpClientParams);
		makeSession(client);

		final PostMethod postMethod = new PostMethod(
				"http://www.magyarorszag.hu:80/kozigazgatas/intezmenyek/onkig/testonk/jegyzo/polghiv/pf/searchofficeinpage/submitOfficeSearch");
		postMethod.setParameter(
				"searchofficeinpage_2{actionForm.typedSettlement}", town);
		postMethod.setParameter(
				"searchofficeinpage_2{actionForm.typedPostCode}",
				postalCode == null ? "" : postalCode);
		postMethod
				.setParameter(
						"searchofficeinpage_2wlw-select_key:{actionForm.selectedSettlementPostCode}",
						"true");
		postMethod.setParameter("x", "8");
		postMethod.setParameter("y", "8");
		client.executeMethod(postMethod);
		return postMethod.getResponseBodyAsString();
	}
}
