package net.anzix.o29.beans;

import java.io.InputStream;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement(name="attachment")
@DiscriminatorValue("attachment")
public class Attachment extends BaseBean{
	
	@Column
	String fileName;
	
	@Column
	String mime;

	@ManyToOne
	@JoinColumn(name="attachedTo_id", nullable=false)
	@XmlTransient
	BaseBean attachedTo;

	@Transient
	@XmlTransient
	InputStream data;

	@Transient
	@XmlTransient
	InputStream thumbnail;

	@XmlTransient
	public InputStream getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(InputStream thumbnail) {
		this.thumbnail = thumbnail;
	}

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
	public InputStream getData() {
		return data;
	}

	public void setData(final InputStream data) {
		this.data = data;
	}

	@XmlTransient
	public BaseBean getAttachedTo() {
		return attachedTo;
	}

	public void setAttachedTo(BaseBean attachedTo) {
		this.attachedTo = attachedTo;
	}
}
