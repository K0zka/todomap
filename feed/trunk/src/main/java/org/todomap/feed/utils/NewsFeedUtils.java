package org.todomap.feed.utils;

import java.util.Arrays;

import org.todomap.feed.beans.AbstractNewsFeed;
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

	public static void setSelfLink(final NewsFeed feed, final String selfLink) {
		final AbstractNewsFeed afeed = ((AbstractNewsFeed) feed);
		final Link link = new Link("self", null, selfLink, null);
		if (afeed.getLinks() == null) {
			afeed.setLinks(Arrays
					.asList(link));
		} else {
			afeed.getLinks().add(link);
		}
	}
}
