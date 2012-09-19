package org.todomap.feed.beans;

import java.util.Date;
import java.util.List;

public interface NewsFeed {
	String getTitle();
	String getDescription();
	Date getLastBuildDate();
	List<NewsItem> getNewsItems();
	List<Link> getLinks();
}
