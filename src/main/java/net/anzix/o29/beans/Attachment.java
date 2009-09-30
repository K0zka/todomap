package net.anzix.o29.beans;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Entity
@DiscriminatorValue("todo")
public class Attachment extends BaseBean{
	
	@Column
	String fileName;
	
	@Column
	String mime;

	@Lob
	@Basic(fetch=FetchType.LAZY)
	byte[] data;

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

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
