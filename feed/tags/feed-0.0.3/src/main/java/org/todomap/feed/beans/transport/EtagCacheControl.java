package org.todomap.feed.beans.transport;

public class EtagCacheControl implements TransportCacheControl {

	public EtagCacheControl(String etag) {
		super();
		this.etag = etag;
	}

	final String etag;

	@Override
	public String toString() {
		return "[etag=" + etag + "]";
	}

	@Override
	public String responseHeaderName() {
		return "ETag";
	}

	@Override
	public String requestHeaderName() {
		return "If-None-Match";
	}

	@Override
	public String getValue() {
		return etag;
	}

}
