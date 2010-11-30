package org.todomap.prototypes.ajaxcleanup.messages;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="subscribe")
public class SubscribeMessage {
	public SubscribeMessage(final String channelName) {
		super();
		this.channelName = channelName;
	}

	public SubscribeMessage() {
		super();
	}

	String channelName;

	@XmlAttribute(name="channel")
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
}
