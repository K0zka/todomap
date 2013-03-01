package org.todomap.feed.beans;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.feed.utils.StringUtils;

@XmlRootElement(name = "link")
public class Link {
	String href;
	String rel;
	String title;
	String type;

	public Link() {
		super();
	}

	public Link(final String rel, final String type, final String href,
			final String title) {
		super();
		this.rel = rel;
		this.type = type;
		this.href = href;
		this.title = title;
	}

	@XmlAttribute(name = "href")
	public String getHref() {
		return href;
	}

	@XmlAttribute(name = "rel")
	public String getRel() {
		return rel;
	}

	@XmlAttribute(name = "title")
	public String getTitle() {
		return title;
	}

	@XmlAttribute(name = "type")
	public String getType() {
		return type;
	}

	public void setHref(final String href) {
		this.href = href;
	}

	public void setRel(final String rel) {
		this.rel = rel;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setType(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "link[rel=" + StringUtils.max(rel, 5) + ", href="
				+ StringUtils.max(href, 20) + "]";
	}
}
