package org.todomap.feed.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.feed.utils.StringUtils;

@XmlRootElement(name = "entry")
public class Entry implements NewsItem {

	String content;
	String id;
	List<Link> links = new ArrayList<Link>();
	Date published;
	String title;
	Date updated;

	@XmlElement(name = "content", namespace = "http://www.w3.org/2005/Atom")
	public String getContent() {
		return content;
	}

	@Override
	public String getDescription() {
		return content;
	}

	@Override
	public String getGuid() {
		return id;
	}

	@XmlElement(name = "id", namespace = "http://www.w3.org/2005/Atom")
	public String getId() {
		return id;
	}

	@XmlElement(name = "link", namespace = "http://www.w3.org/2005/Atom")
	public List<Link> getLinks() {
		return links;
	}

	@Override
	@XmlElement(name = "published", namespace = "http://www.w3.org/2005/Atom")
	public Date getPublished() {
		return published;
	}

	@Override
	@XmlElement(name = "title", namespace = "http://www.w3.org/2005/Atom")
	public String getTitle() {
		return title;
	}

	@XmlElement(name = "updated", namespace = "http://www.w3.org/2005/Atom")
	public Date getUpdated() {
		return updated;
	}

	@Override
	public String getUrl() {
		for (final Link link : links) {
			if ("alternate".equals(link.getRel())) {
				return link.getHref();
			}
		}
		return null;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public void setLinks(final List<Link> links) {
		this.links = links;
	}

	public void setPublished(final Date published) {
		this.published = published;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setUpdated(final Date updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		return "Entry [title=" + StringUtils.max(title, 10) + "]";
	}
}
