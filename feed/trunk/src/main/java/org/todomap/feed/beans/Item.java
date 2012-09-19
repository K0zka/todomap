package org.todomap.feed.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.feed.utils.StringUtils;

@XmlRootElement(name = "item")
public class Item implements NewsItem {
	static final String rssDateFormat = "EEE, dd MMM yyyy hh:mm:ss ZZZ";
	String title;
	String link;
	String description;
	String author;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getPubDate() {
		SimpleDateFormat format = new SimpleDateFormat(rssDateFormat);
		return format.format(pubDate);
	}

	public void setPubDate(String pubDate) throws ParseException {
		this.pubDate = new SimpleDateFormat(rssDateFormat).parse(pubDate);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	String category;
	String comments;
	String guid;
	Date pubDate;
	String source;

	@Override
	public String getUrl() {
		return link;
	}

	@Override
	public String toString() {
		return "Item [title=" + StringUtils.max(title, 10) + "]";
	}
}
