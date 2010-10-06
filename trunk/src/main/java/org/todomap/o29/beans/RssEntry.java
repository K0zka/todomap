package org.todomap.o29.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class RssEntry {
	@ManyToOne
	RssFeed feed;
	@Id
	@GeneratedValue(generator = "rssentry_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "rssentry_seq", allocationSize = 1, initialValue = 0, sequenceName = "rssentry_seq")
	Long id;
	@Column
	String title;
	@Column
	String link;
	@Column
	Date date;
	@Column
	String guid;

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public RssFeed getFeed() {
		return feed;
	}

	public void setFeed(RssFeed feed) {
		this.feed = feed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
}
