package org.todomap.feed.utils;

import org.todomap.feed.beans.Link;
import org.todomap.feed.beans.NewsFeed;

public class NewsFeedUtils {
	public static String findLink(final NewsFeed feed, final String linkType) {
		if (feed == null || feed.getLinks() == null) {
			return null;
		}
		for (final Link link : feed.getLinks()) {
			if (linkType.equalsIgnoreCase(link.getRel())) {
				return link.getHref();
			}
		}
		return null;
	}

	public static String getPubSubHub(final NewsFeed feed) {
		return findLink(feed, "hub");
	}

	public static String getSelfUrl(final NewsFeed feed) {
		return findLink(feed, "self");
	}
}
