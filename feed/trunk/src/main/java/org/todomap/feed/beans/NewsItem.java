package org.todomap.feed.beans;

import java.util.Date;

public interface NewsItem {
	String getDescription();

	String getGuid();

	Date getPublished();

	String getTitle();

	String getUrl();
}
