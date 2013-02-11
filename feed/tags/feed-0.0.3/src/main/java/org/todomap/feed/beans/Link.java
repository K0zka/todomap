package org.todomap.feed.beans;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.feed.utils.StringUtils;

@XmlRootElement(name="link")
public class Link {
	public Link(String rel, String type, String href, String title) {
		super();
		this.rel = rel;
		this.type = type;
		this.href = href;
		this.title = title;
	}
	public Link() {
		super();
	}
	String rel;
	String type;
	String href;
	String title;
	@XmlAttribute(name="rel")
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	@XmlAttribute(name="type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@XmlAttribute(name="href")
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	@XmlAttribute(name="title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "link[rel=" + StringUtils.max(rel, 5) + ", href=" + StringUtils.max(href, 20) + "]";
	}
}
