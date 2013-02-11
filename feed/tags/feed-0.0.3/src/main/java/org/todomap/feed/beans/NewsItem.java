package org.todomap.feed.beans;

import java.util.Date;

public interface NewsItem {
	String getTitle();
	String getDescription();
	String getUrl();
	String getGuid();
	Date getPublished();
}
