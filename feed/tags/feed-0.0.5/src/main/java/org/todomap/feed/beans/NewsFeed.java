package org.todomap.feed.beans;

import java.util.Date;
import java.util.List;

import org.todomap.feed.beans.transport.TransportCacheControl;

public interface NewsFeed {
	List<TransportCacheControl> getCacheControl();

	String getDescription();

	Date getLastBuildDate();

	List<Link> getLinks();

	List<NewsItem> getNewsItems();

	String getTitle();
}
