package org.todomap.spamegg;

public class Content {
	public Content() {
		super();
	}
	public Content(User author, String referrer, String permalink, String type,
			String content) {
		super();
		this.author = author;
		this.referrer = referrer;
		this.permalink = permalink;
		this.type = type;
		this.content = content;
	}
	User author;
	String referrer;
	String permalink;
	String type;
	String content;
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public String getReferrer() {
		return referrer;
	}
	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}
	public String getPermalink() {
		return permalink;
	}
	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
