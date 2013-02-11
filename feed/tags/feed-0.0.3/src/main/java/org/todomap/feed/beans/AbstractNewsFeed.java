package org.todomap.feed.beans;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.todomap.feed.beans.transport.TransportCacheControl;

public abstract class AbstractNewsFeed implements NewsFeed {
	List<TransportCacheControl> cacheControl  = Collections.emptyList();
	@XmlTransient
	public List<TransportCacheControl> getCacheControl() {
		return cacheControl;
	}
	public void setCacheControl(List<TransportCacheControl> cacheControl) {
		this.cacheControl = cacheControl;
	}

}
