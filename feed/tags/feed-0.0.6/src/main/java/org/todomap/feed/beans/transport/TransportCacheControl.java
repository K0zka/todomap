package org.todomap.feed.beans.transport;

public interface TransportCacheControl {
	String getValue();

	String requestHeaderName();

	String responseHeaderName();
}
