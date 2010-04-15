package org.todomap.hu.mohu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.todomap.geocoder.Address;
import org.todomap.geocoder.GeoCodeException;
import org.todomap.geocoder.GeoCoder;
import org.todomap.geocoder.LatLng;
import org.todomap.geocoder.google.GoogleGeocoder;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * mo.hu data access API
 * 
 * @author kocka
 * 
 */
public final class Mohu {

	public Mohu(final GeoCoder geoCoder) {
		super();
		this.geoCoder = geoCoder;
	}

	private final GeoCoder geoCoder;

	/**
	 * 
	 */
	public Mohu() {
		super();
		this.geoCoder = new GoogleGeocoder();
	}

	/**
	 * This class helps to build the contact data from the result xml.
	 * 
	 * @author kocka
	 * 
	 */
	private final static class ResultXmlHandler extends DefaultHandler {
		private final Contact contact;
		private String cssClass;

		private ResultXmlHandler(final Contact contact) {
			this.contact = contact;
		}

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
		public void startElement(final String uri, final String localName,
				final String qName, final Attributes attributes)
				throws SAXException {
			cssClass = attributes.getValue("class");
		}
	}

	private final static SAXParserFactory saxParserFactory = SAXParserFactory
			.newInstance();

	/**
	 * Build a contact from a html fragment fetched from mo.hu.
	 * 
	 * @param htmlFragment
	 *            stringified html
	 * @return contact data
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	Contact buildContactFromHtml(final String htmlFragment)
			throws ParserConfigurationException, SAXException, IOException {
		final Contact contact = new Contact();
		final SAXParser parser = saxParserFactory.newSAXParser();
		parser.parse(new ByteArrayInputStream(htmlFragment.getBytes("UTF-8")),
				new ResultXmlHandler(contact));
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

	/**
	 * Get the contacts by coordinates
	 * 
	 * @param latitude	latitude
	 * @param longitude	longitude
	 * @return
	 * @throws IOException
	 */
	public List<Contact> listContacts(double latitude, double longitude) throws IOException {
		try {
			final Address address = geoCoder.revert(new LatLng(latitude, longitude));
			return listContacts("", address.getTown());
		} catch (final GeoCodeException e) {
			throw new IOException(e);
		}
	}
	
	/**
	 * Get the list of known agencies.
	 * 
	 * @param postalCode
	 *            postal code
	 * @param town
	 *            name of the town
	 * @return List of contacts, which could be empty
	 * @throws IOException
	 */
	public List<Contact> listContacts(final String postalCode, final String town)
			throws IOException {
		final ArrayList<Contact> ret = new ArrayList<Contact>();
		final HtmlCleaner cleaner = new HtmlCleaner();
		final HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams.setParameter("http.useragent",
				"Mozilla/5.0 (X11; U; Linux x86_64; en-US) AppleWebKit/532.9 "
						+ "(KHTML, like Gecko) Chrome/5.0.307.7 Safari/532.9");
		httpClientParams.setContentCharset("UTF-8");
		final HttpClient client = new HttpClient(httpClientParams);

		/*
		 * Make session.
		 */
		client.executeMethod(new GetMethod("http://www.magyarorszag.hu:80"
				+ "/kozigazgatas/intezmenyek/onkig/testonk/jegyzo/polghiv/"));

		/*
		 * Get the list first the usual way, and kick mo.hu if that does nmot
		 * work.
		 */
		TagNode results = getFindNodeByClass(cleaner.clean(makeRequest(
				postalCode, town, client)), "div", "resultset resultset-last");
		if (results == null) {
			results = getFindNodeByClass(cleaner.clean(makeRequestToPostCity(
					town, client)), "div",
					"resultset resultset-last");
		}

		/*
		 * make an xml fragment from the result, this could be suboptimal
		 */
		final StringWriter stringWriter = new StringWriter();
		results.serialize(new PrettyXmlSerializer(cleaner.getProperties()),
				stringWriter);

		/*
		 * Build the contact data.
		 */
		try {
			ret.add(buildContactFromHtml(stringWriter.toString()));
		} catch (final ParserConfigurationException e) {
			throw new IOException(e);
		} catch (final SAXException e) {
			throw new IOException(e);
		}

		return ret;
	}

	/**
	 * Send request to mo.hu the usual way.
	 * 
	 * @param postalCode
	 * @param town
	 * @param client
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private String makeRequest(final String postalCode, final String town,
			final HttpClient client) throws IOException {
		final PostMethod postMethod = new PostMethod(
				"http://www.magyarorszag.hu:80"
						+ "/kozigazgatas/intezmenyek/onkig/testonk/jegyzo/polghiv"
						+ "/pf/searchofficeinpage/submitOfficeSearch");
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

	/**
	 * The foolish way to post the name of the town once again.
	 * @param town
	 * @param client
	 * 
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private String makeRequestToPostCity(final String town,
			final HttpClient client) throws IOException {

		final PostMethod postMethod = new PostMethod(
				"http://www.magyarorszag.hu:80"
						+ "/kozigazgatas/intezmenyek/onkig/testonk/jegyzo/polghiv"
						+ "/pf/searchofficeinpage/submitCity");
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

}