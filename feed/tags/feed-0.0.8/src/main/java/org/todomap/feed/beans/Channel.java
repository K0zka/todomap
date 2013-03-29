package org.todomap.feed.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.feed.utils.StringUtils;

@XmlRootElement(name = "channel")
public class Channel extends AbstractNewsFeed {
	String category;
	String copyright;
	String description;
	String docs;
	String generator;
	List<Item> items;
	String language;
	Date lastBuildDate;
	String link;
	String managingEditor;
	Date pubDate;
	String title;
	int ttl;
	String webMaster;

	@XmlElement(name = "category")
	public String getCategory() {
		return category;
	}

	@XmlElement
	public String getCopyright() {
		return copyright;
	}

	@Override
	@XmlElement(name = "description", required = true)
	public String getDescription() {
		return description;
	}

	@XmlElement(name = "docs")
	public String getDocs() {
		return docs;
	}

	@XmlElement
	public String getGenerator() {
		return generator;
	}

	@XmlElement(name = "item")
	public List<Item> getItems() {
		return items;
	}

	@XmlElement(name = "language")
	public String getLanguage() {
		return language;
	}

	@Override
	@XmlElement
	public Date getLastBuildDate() {
		return lastBuildDate;
	}

	@XmlElement(name = "link", required = true)
	public String getLink() {
		return link;
	}

	@XmlElement
	public String getManagingEditor() {
		return managingEditor;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<NewsItem> getNewsItems() {
		return ((List) items);
	}

	@XmlElement(name = "pubDate")
	public String getPubDate() {
		if (pubDate == null) {
			return null;
		} else {
			return new SimpleDateFormat(Item.rssDateFormat).format(pubDate);
		}
	}

	@Override
	@XmlElement(name = "title", required = true)
	public String getTitle() {
		return title;
	}

	@XmlElement(name = "ttl")
	public int getTtl() {
		return ttl;
	}

	@XmlElement(name = "webMaster")
	public String getWebMaster() {
		return webMaster;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

	public void setCopyright(final String copyright) {
		this.copyright = copyright;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setDocs(final String docs) {
		this.docs = docs;
	}

	public void setGenerator(final String generator) {
		this.generator = generator;
	}

	public void setItems(final List<Item> items) {
		this.items = items;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

	public void setLastBuildDate(final Date lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}

	public void setLink(final String link) {
		this.link = link;
	}

	public void setManagingEditor(final String managingEditor) {
		this.managingEditor = managingEditor;
	}

	public void setPubDate(final Date pubDate) {
		this.pubDate = pubDate;
	}

	public void setPubDate(final String pubDate) throws ParseException {
		if (pubDate == null) {
			this.pubDate = null;
		} else {
			this.pubDate = new SimpleDateFormat(Item.rssDateFormat)
					.parse(pubDate);
		}

	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setTtl(final int ttl) {
		this.ttl = ttl;
	}

	public void setWebMaster(final String webMaster) {
		this.webMaster = webMaster;
	}

	@Override
	public String toString() {
		return "Channel [title=" + StringUtils.max(title, 10) + ", items="
				+ items.size() + "]";
	}

}
