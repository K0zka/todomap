package org.todomap.feed.beans;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="entry")
public class Entry {
	String title;
	String id;
	Date updated;
	Date published;
	String content;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public Date getPublished() {
		return published;
	}
	public void setPublished(Date published) {
		this.published = published;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
