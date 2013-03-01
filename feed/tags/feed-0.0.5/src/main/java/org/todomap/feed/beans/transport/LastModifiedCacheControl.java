package org.todomap.feed.beans.transport;

public class LastModifiedCacheControl implements TransportCacheControl {

	final String value;

	public LastModifiedCacheControl(final String value) {
		super();
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String requestHeaderName() {
		return "If-Modified-Since";
	}

	@Override
	public String responseHeaderName() {
		return "Last-Modified";
	}

	@Override
	public String toString() {
		return "[Last-Modified=" + value + "]";
	}

}
