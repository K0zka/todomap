package org.todomap.feed.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.feed.utils.StringUtils;

@XmlRootElement(name = "item")
public class Item implements NewsItem {
	static final String rssDateFormat = "EEE, dd MMM yyyy hh:mm:ss ZZZ";
	String author;
	String category;
	String comments;
	String description;

	String guid;

	String link;

	Date pubDate;

	String source;

	String title;

	public String getAuthor() {
		return author;
	}

	public String getCategory() {
		return category;
	}

	public String getComments() {
		return comments;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getGuid() {
		return guid;
	}

	public String getLink() {
		return link;
	}

	public String getPubDate() {
		final SimpleDateFormat format = new SimpleDateFormat(rssDateFormat);
		return format.format(pubDate);
	}

	@Override
	public Date getPublished() {
		return pubDate;
	}

	public String getSource() {
		return source;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getUrl() {
		return link;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

	public void setComments(final String comments) {
		this.comments = comments;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setGuid(final String guid) {
		this.guid = guid;
	}

	public void setLink(final String link) {
		this.link = link;
	}

	public void setPubDate(final String pubDate) throws ParseException {
		try {
			this.pubDate = new SimpleDateFormat(rssDateFormat).parse(pubDate);
		} catch (final ParseException pe) {
			// ignore, the rest may have some sense :(
		}
	}

	public void setSource(final String source) {
		this.source = source;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Item [title=" + StringUtils.max(title, 10) + "]";
	}
}
