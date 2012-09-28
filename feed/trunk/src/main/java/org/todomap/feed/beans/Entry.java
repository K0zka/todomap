package org.todomap.feed.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.feed.utils.StringUtils;

@XmlRootElement(name = "entry")
public class Entry implements NewsItem {

	String title;
	String id;
	Date updated;
	Date published;
	String content;
	List<Link> links = new ArrayList<Link>();

	@XmlElement(name = "title", namespace = "http://www.w3.org/2005/Atom")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement(name = "id", namespace = "http://www.w3.org/2005/Atom")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement(name = "updated", namespace = "http://www.w3.org/2005/Atom")
	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@XmlElement(name = "published", namespace = "http://www.w3.org/2005/Atom")
	public Date getPublished() {
		return published;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	@XmlElement(name = "content", namespace = "http://www.w3.org/2005/Atom")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getDescription() {
		return content;
	}

	@XmlElement(name = "link", namespace = "http://www.w3.org/2005/Atom")
	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	@Override
	public String getUrl() {
		for (Link link : links) {
			if ("alternate".equals(link.getRel())) {
				return link.getHref();
			}
		}
		return null;
	}

	@Override
	public String getGuid() {
		return id;
	}

	@Override
	public String toString() {
		return "Entry [title=" + StringUtils.max(title, 10) + "]";
	}
}
