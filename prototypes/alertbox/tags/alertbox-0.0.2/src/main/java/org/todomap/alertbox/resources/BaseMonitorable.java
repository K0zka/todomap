package org.todomap.alertbox.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.todomap.alertbox.Monitorable;

public abstract class BaseMonitorable implements Monitorable {
	String docUrl;
	String id;
	String[] tags = new String[]{};
	final long created = System.currentTimeMillis();

	@XmlAttribute(name="doc")
	public final String getDocUrl() {
		return docUrl;
	}

	public final void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	public final String getId() {
		return id;
	}

	public final void setId(final String id) {
		this.id = id;
	}

	@XmlElement(name="tag")
	public final String[] getTags() {
		return tags;
	}

	public final void setTags(final String[] tags) {
		this.tags = tags;
	}

	@Override
	public long lastChanged() {
		return created;
	}
}
