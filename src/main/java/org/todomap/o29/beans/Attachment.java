package org.todomap.o29.beans;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement(name="attachment")
@DiscriminatorValue("attachment")
public class Attachment extends BaseBean implements Translatable {
	
	@Column
	String fileName;

	@Column
	String description;
	
	@Column
	String mime;

	@ManyToOne
	@JoinColumn(name="attachedTo_id", nullable=false)
	@XmlTransient
	BaseBean attachedTo;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	@XmlTransient
	public BaseBean getAttachedTo() {
		return attachedTo;
	}

	public void setAttachedTo(BaseBean attachedTo) {
		this.attachedTo = attachedTo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getText() {
		return getDescription();
	}
}
