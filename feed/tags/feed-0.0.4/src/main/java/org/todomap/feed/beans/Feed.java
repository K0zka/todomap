package org.todomap.feed.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="feed", namespace="http://www.w3.org/2005/Atom")
public class Feed extends AbstractNewsFeed {
	String title;
	String subtitle;
	Date updated;

	List<Link> links = new ArrayList<Link>();

	List<Entry> entries = new ArrayList<Entry>();

	@XmlElement(name="title", namespace="http://www.w3.org/2005/Atom")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@XmlElement(name="subtitle", namespace="http://www.w3.org/2005/Atom")
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	@XmlElement(name="updated", namespace="http://www.w3.org/2005/Atom")
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	@XmlElement(name="id", namespace="http://www.w3.org/2005/Atom")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRights() {
		return rights;
	}
	public void setRights(String rights) {
		this.rights = rights;
	}
	public String getGenerator() {
		return generator;
	}
	public void setGenerator(String generator) {
		this.generator = generator;
	}
	String id;
	String rights;
	String generator;

	@XmlElement(name="entry", namespace="http://www.w3.org/2005/Atom")
	public List<Entry> getEntries() {
		return entries;
	}
	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	@Override
	public String getDescription() {
		return subtitle;
	}
	@Override
	public Date getLastBuildDate() {
		return updated;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<NewsItem> getNewsItems() {
		return (List)entries;
	}
	@XmlElement(name="link", namespace="http://www.w3.org/2005/Atom")
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
}
