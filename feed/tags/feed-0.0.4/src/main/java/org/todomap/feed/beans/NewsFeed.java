package org.todomap.feed.beans;

import java.util.Date;
import java.util.List;

import org.todomap.feed.beans.transport.TransportCacheControl;

public interface NewsFeed {
	String getTitle();

	String getDescription();

	Date getLastBuildDate();

	List<NewsItem> getNewsItems();

	List<Link> getLinks();

	List<TransportCacheControl> getCacheControl();
}
