package org.todomap.feed.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "feed", namespace = "http://www.w3.org/2005/Atom")
public class Feed extends AbstractNewsFeed {
	List<Entry> entries = new ArrayList<Entry>();
	String generator;
	String id;

	String rights;

	String subtitle;
	String title;
	Date updated;

	@Override
	public String getDescription() {
		return subtitle;
	}

	@XmlElement(name = "entry", namespace = "http://www.w3.org/2005/Atom")
	public List<Entry> getEntries() {
		return entries;
	}

	public String getGenerator() {
		return generator;
	}

	@XmlElement(name = "id", namespace = "http://www.w3.org/2005/Atom")
	public String getId() {
		return id;
	}

	@Override
	public Date getLastBuildDate() {
		return updated;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<NewsItem> getNewsItems() {
		return (List) entries;
	}

	public String getRights() {
		return rights;
	}

	@XmlElement(name = "subtitle", namespace = "http://www.w3.org/2005/Atom")
	public String getSubtitle() {
		return subtitle;
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

	public void setEntries(final List<Entry> entries) {
		this.entries = entries;
	}

	public void setGenerator(final String generator) {
		this.generator = generator;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public void setRights(final String rights) {
		this.rights = rights;
	}

	public void setSubtitle(final String subtitle) {
		this.subtitle = subtitle;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setUpdated(final Date updated) {
		this.updated = updated;
	}

}
