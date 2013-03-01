package org.todomap.feed.beans;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rss")
public class Rss {
	Channel channel;
	String version = "2.0";

	@XmlElement(name = "channel")
	public Channel getChannel() {
		return channel;
	}

	@XmlAttribute(name = "version")
	public String getVersion() {
		return version;
	}

	public void setChannel(final Channel channel) {
		this.channel = channel;
	}

	public void setVersion(final String version) {
		this.version = version;
	}
}
