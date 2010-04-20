package org.todomap.o29.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProjectRssEntry {
	@ManyToOne
	ProjectRssFeed feed;
	@Id
	String id;
	@Column
	String title;
	@Column
	String link;
	public ProjectRssFeed getFeed() {
		return feed;
	}
	public void setFeed(ProjectRssFeed feed) {
		this.feed = feed;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
