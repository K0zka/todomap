package org.todomap.hu.mohu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Mohu {

	private final static SAXParserFactory saxParserFactory = SAXParserFactory
			.newInstance();

	Contact buildContactFromHtml(final String htmlFragment)
			throws ParserConfigurationException, SAXException, IOException {
		final Contact contact = new Contact();
		final SAXParser parser = saxParserFactory.newSAXParser();
		parser.parse(new ByteArrayInputStream(htmlFragment.getBytes("UTF-8")),
				new DefaultHandler() {

					String cssClass;

					@Override
					public void characters(final char[] ch, final int start,
							final int length) throws SAXException {
						final String value = new String(ch, start, length);
						if ("title".equals(cssClass)) {
							contact.setName(value);
						} else if ("address".equals(cssClass)) {
							contact.setAddress(value);
						} else if ("phone".equals(cssClass)) {
							contact.setPhone(value.replace("Telefon: ", ""));
						}
					}

					@Override
					public void endElement(final String uri,
							final String localName, final String qName)
							throws SAXException {
					}

					@Override
					public void startElement(final String uri,
							final String localName, final String qName,
							final Attributes attributes) throws SAXException {
						cssClass = attributes.getValue("class");
					}
				});
		return contact;
	}

	@SuppressWarnings("unchecked")
	private TagNode getFindNodeByClass(final TagNode node, final String name,
			final String cssClass) {
		final List<TagNode> children = node.getAllElementsList(true);
		for (final TagNode child : children) {
			if (name.equals((child).getName())
					&& cssClass.equals((child).getAttributeByName("class"))) {
				return child;
			} else {
				final TagNode grandchild = getFindNodeByClass(child, name,
						cssClass);
				if (grandchild != null) {
					return grandchild;
				}
			}
		}
		return null;
	}

	List<Contact> listContacts(final String postalCode, final String town)
			throws MalformedURLException, IOException {
		final ArrayList<Contact> ret = new ArrayList<Contact>();
		final HtmlCleaner cleaner = new HtmlCleaner();
		final HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams
				.setParameter(
						"http.useragent",
						"Mozilla/5.0 (X11; U; Linux x86_64; en-US) AppleWebKit/532.9 (KHTML, like Gecko) Chrome/5.0.307.7 Safari/532.9");
		httpClientParams.setContentCharset("UTF-8");
		final HttpClient client = new HttpClient(httpClientParams);

		makeSession(client);
		TagNode results = getFindNodeByClass(cleaner.clean(makeRequest(
				postalCode, town, client)), "div", "resultset resultset-last");
		if (results == null) {
			results = getFindNodeByClass(cleaner.clean(makeRequestToPostCity(
					postalCode, town, client)), "div",
					"resultset resultset-last");
		}

		final StringWriter stringWriter = new StringWriter();
		results.serialize(new PrettyXmlSerializer(cleaner.getProperties()),
				stringWriter);

		try {
			ret.add(buildContactFromHtml(stringWriter.toString()));
		} catch (final ParserConfigurationException e) {
			throw new IOException(e);
		} catch (final SAXException e) {
			throw new IOException(e);
		}

		return ret;
	}

	private String makeRequest(final String postalCode, final String town,
			final HttpClient client) throws IOException, MalformedURLException,
			UnsupportedEncodingException {
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

	private String makeRequestToPostCity(final String postalCode,
			final String town, final HttpClient client) throws IOException,
			MalformedURLException, UnsupportedEncodingException {

		final PostMethod postMethod = new PostMethod(
				"http://www.magyarorszag.hu:80/kozigazgatas/intezmenyek/onkig/testonk/jegyzo/polghiv/pf/searchofficeinpage/submitCity");
		postMethod
				.setParameter(
						"searchofficeinpage_2wlw-select_key:{actionForm.selectedSettlementPostCode}",
						town);
		postMethod
				.setParameter(
						"searchofficeinpage_2wlw-select_key:{actionForm.selectedSettlementPostCode}OldValue",
						"true");
		postMethod.setParameter("x", "10");
		postMethod.setParameter("y", "13");
		client.executeMethod(postMethod);
		return postMethod.getResponseBodyAsString();
	}

	private void makeSession(final HttpClient client) throws IOException,
			HttpException {
		client
				.executeMethod(new GetMethod(
						"http://www.magyarorszag.hu:80/kozigazgatas/intezmenyek/onkig/testonk/jegyzo/polghiv/"));
	}

}
