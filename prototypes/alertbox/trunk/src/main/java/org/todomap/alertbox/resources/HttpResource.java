package org.todomap.alertbox.resources;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

@XmlRootElement(name="http")
public final class HttpResource extends BaseMonitorable {

	String url;
	String[] failRegexps = new String[]{};
	Integer statusCode;

	@Override
	public StatusDescription check() throws Exception {
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(url);
		client.executeMethod(get);
		if (statusCode != null && get.getStatusCode() != statusCode) {
			return new StatusDescription(Status.Fail, "Returned status code "
					+ get.getStatusCode());
		}
		List<String> response = IOUtils
				.readLines(get.getResponseBodyAsStream());
		for (String line : response) {
			for (String failRegexp : failRegexps) {
				if (line.matches(failRegexp)) {
					return new StatusDescription(Status.Fail,
							"Matched fail regexp:" + failRegexp);
				}
			}
		}
		return new StatusDescription(Status.Ok, "");
	}

	@Override
	public String getName() {
		return url + " http check";
	}

	@XmlAttribute(name="url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@XmlElement(name="fail-if-match")
	public String[] getFailRegexps() {
		return failRegexps;
	}

	public void setFailRegexps(String[] failRegexps) {
		this.failRegexps = failRegexps;
	}

	@Override
	public String getTypeId() {
		return "http";
	}

	@XmlAttribute(name="status")
	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

}
