package org.todomap.prototypes.ajaxcleanup.messages;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="unsubscribe")
public class UnsubscribeMessage {
	public UnsubscribeMessage(final String name) {
		super();
		this.name = name;
	}

	public UnsubscribeMessage() {
		super();
	}

	String name;

	@XmlAttribute(name="channel")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
