package org.todomap.microurl;

public interface ManageableUrlShortener extends UrlShortener{
	long getClicks(String url) throws UrlShortenerException;
}
