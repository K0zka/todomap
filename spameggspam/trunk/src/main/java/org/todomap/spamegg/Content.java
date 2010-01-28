package org.todomap.spamegg;

/**
 * The content representation bean.
 * 
 * @author kocka
 */
public final class Content {
	/** Author of the content */
	private final User author;
	/** The content */
	private final String content;
	private final String permalink;
	private final String referrer;
	private final String type;

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
}
