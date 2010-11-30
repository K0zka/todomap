package org.todomap.prototypes.ajaxcleanup.messages;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="update")
public class UpdateMessage {
	public UpdateMessage() {
		super();
	}
	public UpdateMessage(String channel, String message) {
		super();
		this.channel = channel;
		this.message = message;
	}
	String channel;
	String message;
	@XmlAttribute(name="channel")
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	@XmlElement(nillable=false)
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
