package org.todomap.feed.utils;

import org.todomap.feed.beans.Link;
import org.todomap.feed.beans.NewsFeed;

public class NewsFeedUtils {
	public static String getPubSubHub(final NewsFeed feed) {
		if (feed.getLinks() == null) {
			return null;
		}
		for (Link link : feed.getLinks()) {
			if ("hub".equalsIgnoreCase(link.getRel())) {
				return link.getHref();
			}
		}
		return null;
	}
}
