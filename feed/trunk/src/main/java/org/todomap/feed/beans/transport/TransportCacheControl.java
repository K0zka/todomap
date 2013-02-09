package org.todomap.feed.beans.transport;

public interface TransportCacheControl {
	String responseHeaderName();
	String requestHeaderName();
	String getValue();
}
