package org.todomap.feed.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="channel")
public class Channel {
	String title;
	String link;
	String description;
	Date lastBuildDate;
	String generator;
	String language;
	String copyright;
	String managingEditor;
	String webMaster;
	Date pubDate;
	String category;
	String docs;
	int ttl;
	List<Item> items;


	@XmlElement(name = "title", required = true)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement(name = "link", required = true)
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@XmlElement(name = "description", required = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement
	public Date getLastBuildDate() {
		return lastBuildDate;
	}

	public void setLastBuildDate(Date lastBuildDate) {
		this.lastBuildDate = lastBuildDate;
	}

	@XmlElement
	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	@XmlElement(name="language")
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@XmlElement
	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	@XmlElement
	public String getManagingEditor() {
		return managingEditor;
	}

	public void setManagingEditor(String managingEditor) {
		this.managingEditor = managingEditor;
	}

	@XmlElement(name="webMaster")
	public String getWebMaster() {
		return webMaster;
	}

	public void setWebMaster(String webMaster) {
		this.webMaster = webMaster;
	}

	@XmlElement(name="pubDate")
	public String getPubDate() {
		if(pubDate == null) {
			return null;
		} else {
			return new SimpleDateFormat(Item.rssDateFormat).format(pubDate);
		}
	}

	public void setPubDate(String pubDate) throws ParseException {
		if(pubDate == null) {
			this.pubDate = null;
		} else {
			this.pubDate = new SimpleDateFormat(Item.rssDateFormat).parse(pubDate);
		}
		
	}

	@XmlElement(name="category")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@XmlElement(name="docs")
	public String getDocs() {
		return docs;
	}

	public void setDocs(String docs) {
		this.docs = docs;
	}

	@XmlElement(name="ttl")
	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	@XmlElement(name="item")
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

}
