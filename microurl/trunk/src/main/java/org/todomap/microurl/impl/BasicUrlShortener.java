package org.todomap.microurl.impl;

import org.todomap.microurl.UrlShortener;

public abstract class BasicUrlShortener implements UrlShortener {

	private String key;

	public final String getKey() {
		return key;
	}

	public final void setKey(final String key) {
		this.key = key;
	}

}
