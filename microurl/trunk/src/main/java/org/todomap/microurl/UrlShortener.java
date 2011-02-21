package org.todomap.microurl;

public interface UrlShortener {
	String shorten(String url) throws UrlShortenerException;
	String expand(String url) throws UrlShortenerException;
}
