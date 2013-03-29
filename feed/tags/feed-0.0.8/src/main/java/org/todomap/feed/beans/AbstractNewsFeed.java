package org.todomap.feed.beans;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.todomap.feed.beans.transport.TransportCacheControl;

public abstract class AbstractNewsFeed implements NewsFeed {
	List<TransportCacheControl> cacheControl = Collections.emptyList();
	List<Link> links;

	/**
	 * Transport compression.
	 */
	boolean transportCompressed;

	@Override
	@XmlTransient
	public List<TransportCacheControl> getCacheControl() {
		return cacheControl;
	}

	@Override
	@XmlElement(name = "link", namespace = "http://www.w3.org/2005/Atom")
	public List<Link> getLinks() {
		return links;
	}

	@Override
	@XmlTransient
	public boolean isTransportCompressed() {
		return transportCompressed;
	}

	public void setCacheControl(final List<TransportCacheControl> cacheControl) {
		this.cacheControl = cacheControl;
	}

	public void setLinks(final List<Link> links) {
		this.links = links;
	}

	public void setTransportCompressed(final boolean transportCompressed) {
		this.transportCompressed = transportCompressed;
	}

}
