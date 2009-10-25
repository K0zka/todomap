package net.anzix.o29.beans;

import java.io.InputStream;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("todo")
public class Attachment extends BaseBean{
	
	@Column
	String fileName;
	
	@Column
	String mime;

	@ManyToOne
	BaseBean attachedTo;
	
	@Transient
	InputStream data;

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

	public InputStream getData() {
		return data;
	}

	public void setData(final InputStream data) {
		this.data = data;
	}

	public BaseBean getAttachedTo() {
		return attachedTo;
	}

	public void setAttachedTo(BaseBean attachedTo) {
		this.attachedTo = attachedTo;
	}
}
