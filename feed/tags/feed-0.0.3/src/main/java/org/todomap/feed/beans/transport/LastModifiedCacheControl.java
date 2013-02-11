package org.todomap.feed.beans.transport;


public class LastModifiedCacheControl implements TransportCacheControl {

	public LastModifiedCacheControl(String value) {
		super();
		this.value = value;
	}

	final String value;

	@Override
	public String responseHeaderName() {
		return "Last-Modified";
	}
	
	@Override
	public String requestHeaderName() {
		return "If-Modified-Since";
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "[Last-Modified=" + value + "]";
	}

}
