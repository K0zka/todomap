package org.todomap.feed.beans.transport;

public class EtagCacheControl implements TransportCacheControl {

	final String etag;

	public EtagCacheControl(final String etag) {
		super();
		this.etag = etag;
	}

	@Override
	public String getValue() {
		return etag;
	}

	@Override
	public String requestHeaderName() {
		return "If-None-Match";
	}

	@Override
	public String responseHeaderName() {
		return "ETag";
	}

	@Override
	public String toString() {
		return "[etag=" + etag + "]";
	}

}
