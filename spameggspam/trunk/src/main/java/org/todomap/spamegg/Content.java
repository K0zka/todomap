package org.todomap.spamegg;

public class Content {
	private User author;
	private String content;
	private String permalink;
	private String referrer;
	private String type;

	public Content() {
		super();
	}

	public Content(final User author, final String referrer,
			final String permalink, final String type, final String content) {
		super();
		this.author = author;
		this.referrer = referrer;
		this.permalink = permalink;
		this.type = type;
		this.content = content;
	}

	public User getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public String getPermalink() {
		return permalink;
	}

	public String getReferrer() {
		return referrer;
	}

	public String getType() {
		return type;
	}

	public void setAuthor(final User author) {
		this.author = author;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public void setPermalink(final String permalink) {
		this.permalink = permalink;
	}

	public void setReferrer(final String referrer) {
		this.referrer = referrer;
	}

	public void setType(final String type) {
		this.type = type;
	}
}
