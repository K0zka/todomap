package org.todomap.feed.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rss")
public class Rss {
	String version = "2.0";
	@XmlAttribute(name="version")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	List<Channel> channels = new ArrayList<Channel>();
	@XmlElement(name="channel")
	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
}
