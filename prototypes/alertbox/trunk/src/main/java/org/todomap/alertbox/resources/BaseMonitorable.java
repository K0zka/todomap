package org.todomap.alertbox.resources;

import javax.xml.bind.annotation.XmlAttribute;

import org.todomap.alertbox.Monitorable;

public abstract class BaseMonitorable implements Monitorable {
	String docUrl;

	@XmlAttribute(name="doc")
	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}
}
